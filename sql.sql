/*1 Task*/
SELECT Employees.Name
FROM Employees, Employees AS Boss
WHERE (Employees.BossID = Boss.EmployeeID)
		AND(Employees.Salary > Boss.Salary)
);

/*2 Task*/
SELECT Employees.*
FROM Employees
INNER JOIN ((SELECT Employees.DepartmentID AS dep_id, max(Employees.Salary) AS max_sul
		FROM Employees GROUP BY Employees.DepartmentID) AS tab)
ON (Employees.DepartmentID=tab.dep_id AND Employees.Salary=tab.max_sul);

/*3 Task*/
SELECT Departments.Name
FROM (Employees INNER JOIN Departments ON Employees.DepartmentID=Departments.DepartmentID)
	INNER JOIN ((SELECT Employees.DepartmentID as dep_id, count(*) as num_emp
		FROM Employees GROUP BY Employees.DepartmentID) AS tab) ON (Employees.DepartmentID=tab.dep_id)
WHERE tab.num_emp<3
GROUP BY Departments.DepartmentID, Departments.Name;

/*4 Task*/
SELECT Employees.*
FROM Employees LEFT JOIN 
((SELECT Employees.EmployeeID AS emp_id
FROM Employees INNER JOIN ((SELECT Employees.DepartmentID AS dep_id, Employees.EmployeeID AS bos_emp_id
		FROM Employees) AS Boss)
ON (Employees.BossID=Boss.bos_emp_id AND Employees.DepartmentID=Boss.dep_id)) AS emps)
ON (Employees.EmployeeID = emps.emp_id)
WHERE emps.emp_id IS NULL;

/*5 Task*/
SELECT Employees.DepartmentID, SUM(Employees.Salary) 
FROM Employees
GROUP BY Employees.DepartmentID;









