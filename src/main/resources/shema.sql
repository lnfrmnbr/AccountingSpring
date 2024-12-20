CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    father_name VARCHAR(20),
    last_name VARCHAR(20) NOT NULL,
    position VARCHAR(20),
    salary INT
);

CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS projects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    cost INT,
    department_id INT,
    date_beg DATE,
    date_end DATE,
    date_end_real DATE,
	FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS departments_employees (
    id SERIAL PRIMARY KEY,
    department_id INT,
    employee_id INT,
    UNIQUE(department_id, employee_id),
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    admin BOOLEAN
);

CREATE OR REPLACE FUNCTION check_employee_in_department()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM departments_employees
        WHERE department_id = NEW.department_id AND employee_id = NEW.employee_id
    ) THEN
        RAISE EXCEPTION 'Employee already exists in this department';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_check_employee_in_department
BEFORE INSERT ON departments_employees
FOR EACH ROW EXECUTE FUNCTION check_employee_in_department();

CREATE OR REPLACE FUNCTION check_project_dates()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.date_beg IS NULL THEN
        RAISE EXCEPTION 'Start date of the project must be specified';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_check_project_dates
BEFORE INSERT ON projects
FOR EACH ROW EXECUTE FUNCTION check_project_dates();

CREATE OR REPLACE FUNCTION check_project_end_date()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.date_end < NEW.date_beg THEN
        RAISE EXCEPTION 'End date cannot be earlier than start date';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_check_project_end_date
BEFORE INSERT OR UPDATE ON projects
FOR EACH ROW EXECUTE FUNCTION check_project_end_date();

CREATE OR REPLACE FUNCTION get_average_salary(department_id INT)
RETURNS NUMERIC AS $$
DECLARE
    avg_salary NUMERIC;
BEGIN
    SELECT AVG(salary) INTO avg_salary
    FROM employees e
    JOIN departments_employees de ON e.id = de.employee_id
    WHERE de.department_id = department_id;

    RETURN avg_salary;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION public.calculate_profit(
	p_project_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    total_profit INT := 0;
    project_cost   INT;
    total_expense  INT := 0;
    project_beg TIMESTAMP;
    project_end TIMESTAMP;
    employee_salary INT;
BEGIN
    -- Получение данных о проекте
    SELECT p.cost, p.date_beg, p.date_end
    INTO project_cost, project_beg, project_end
    FROM projects p
    WHERE p.id = p_project_id AND p.date_end > CURRENT_TIMESTAMP;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Project with ID % does not exist or is already completed', p_project_id;
    END IF;

    -- Вычисление затрат на проект до текущей даты
    SELECT COALESCE(SUM(e.salary * (EXTRACT(MONTH FROM age(project_end, project_beg)) + 1)), 0)
    INTO total_expense
    FROM departments_employees de
    JOIN employees e ON de.employee_id = e.id
    WHERE de.department_id IN (SELECT department_id FROM projects WHERE id = p_project_id);

    -- Вычисление прибыли от незавершенного проекта
    total_profit := project_cost - total_expense;

    RETURN total_profit;
END;
$BODY$;

CREATE OR REPLACE FUNCTION calculate_profit()
RETURNS INT AS $$
DECLARE
    total_profit INT := 0;
    project_cost   INT;
    total_expense  INT;
    project_id     INT;
    project_beg TIMESTAMP;
    project_end TIMESTAMP;
    employee_salary INT;
    cursor_projects CURSOR FOR
        SELECT p.id, p.cost, p.date_beg, p.date_end
        FROM projects p
        WHERE p.date_end > CURRENT_TIMESTAMP;
BEGIN
    OPEN cursor_projects;

    LOOP
        FETCH cursor_projects INTO project_id, project_cost, project_beg, project_end;
        EXIT WHEN NOT FOUND;

        SELECT COALESCE(SUM(e.salary * (EXTRACT(MONTH FROM age(project_end, project_beg)) + 1)), 0)
        INTO total_expense
        FROM departments_employees de
        JOIN employees e ON de.employee_id = e.id
        WHERE de.department_id IN (SELECT department_id FROM projects WHERE id = project_id);

        total_profit := total_profit + (project_cost - total_expense);
    END LOOP;

    CLOSE cursor_projects;

    RETURN total_profit;
END;
$$ LANGUAGE plpgsql;








