-- =============================================
-- V2__seed_data.sql
-- Seed data for NeuroForge platform
-- =============================================

-- Seed Roles (only if they don't exist)
INSERT INTO dbo.Roles (RoleName, Description)
SELECT * FROM (VALUES
    ('System Administrator', 'Creates accounts, assigns roles/permissions, configures settings, monitors system health.'),
    ('Project Manager',      'Plans and manages projects, creates schedules, assigns tasks, tracks progress and resources.'),
    ('Business Analyst',     'Collects and analyses requirements from clients/stakeholders and prepares requirement documents.'),
    ('Product Owner',        'Defines product vision, prioritises the backlog and approves requirements.'),
    ('Software Architect',   'Designs system architecture, selects technologies and defines coding standards.'),
    ('Developer',            'Develops features, writes/reviews code, fixes bugs and integrates APIs with AI assistance.'),
    ('QA / Test Engineer',   'Creates test cases, performs manual/automated testing and verifies bug fixes.'),
    ('DevOps Engineer',      'Manages CI/CD pipelines, automates builds/deployments and monitors infrastructure.'),
    ('Client / Stakeholder', 'Reviews progress via dashboards, approves deliverables and confirms requirements are met.')
) AS Data(RoleName, Description)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Roles WHERE RoleName = Data.RoleName);
GO

-- Seed Modules (only if they don't exist)
INSERT INTO dbo.Modules (ModuleName, Description)
SELECT * FROM (VALUES
    ('Administration',        'Account, role and system-settings management.'),
    ('ProjectManagement',     'Project creation, membership and scheduling.'),
    ('RequirementCapture',    'Entering requirements and AI-generated user stories.'),
    ('Design',                'AI-assisted architecture/design suggestions.'),
    ('Implementation',        'Task management and AI code suggestions.'),
    ('Testing',               'Test-case generation, review and execution.'),
    ('IssueTracking',         'Defect logging, assignment and resolution.'),
    ('Deployment',            'CI/CD pipeline status and release tracking.'),
    ('Dashboard',             'Project dashboards and analytics.')
) AS Data(ModuleName, Description)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Modules WHERE ModuleName = Data.ModuleName);
GO

-- Grant all permissions to System Administrator
INSERT INTO dbo.RolePermissions (RoleID, ModuleID, CanView, CanCreate, CanEdit, CanDelete)
SELECT r.RoleID, m.ModuleID, 1, 1, 1, 1
FROM dbo.Roles r CROSS JOIN dbo.Modules m
WHERE r.RoleName = 'System Administrator'
AND NOT EXISTS (
    SELECT 1 FROM dbo.RolePermissions rp 
    WHERE rp.RoleID = r.RoleID AND rp.ModuleID = m.ModuleID
);
GO

-- Grant view-only access to Dashboard for all other roles
INSERT INTO dbo.RolePermissions (RoleID, ModuleID, CanView, CanCreate, CanEdit, CanDelete)
SELECT r.RoleID, m.ModuleID, 1, 0, 0, 0
FROM dbo.Roles r CROSS JOIN dbo.Modules m
WHERE r.RoleName <> 'System Administrator' AND m.ModuleName = 'Dashboard'
AND NOT EXISTS (
    SELECT 1 FROM dbo.RolePermissions rp 
    WHERE rp.RoleID = r.RoleID AND rp.ModuleID = m.ModuleID
);
GO

-- Create default admin user (only if it doesn't exist)
INSERT INTO dbo.Users (
    FullName, 
    Email, 
    PasswordHash, 
    RoleID, 
    IsActive, 
    IsArchived,
    CreatedAt
)
SELECT 
    'System Administrator',
    'admin@neuroforge.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E',  -- Password: Admin@123
    (SELECT RoleID FROM dbo.Roles WHERE RoleName = 'System Administrator'),
    1,
    0,
    SYSUTCDATETIME()
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Users WHERE Email = 'admin@neuroforge.com'
);
GO