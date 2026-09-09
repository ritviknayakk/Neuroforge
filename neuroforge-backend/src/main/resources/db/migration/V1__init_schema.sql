-- =============================================
-- V1__init_schema.sql
-- Initial schema for NeuroForge platform
-- =============================================

-- 1. ROLES  
CREATE TABLE dbo.Roles (
    RoleID          INT IDENTITY(1,1)   NOT NULL,
    RoleName        NVARCHAR(50)        NOT NULL,
    Description     NVARCHAR(255)       NULL,
    CreatedAt       DATETIME2(0)        NOT NULL CONSTRAINT DF_Roles_CreatedAt DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT PK_Roles PRIMARY KEY CLUSTERED (RoleID),
    CONSTRAINT UQ_Roles_RoleName UNIQUE (RoleName)
);
GO

-- 2. MODULES 
CREATE TABLE dbo.Modules (
    ModuleID        INT IDENTITY(1,1)   NOT NULL,
    ModuleName      NVARCHAR(50)        NOT NULL,
    Description     NVARCHAR(255)       NULL,
    CONSTRAINT PK_Modules PRIMARY KEY CLUSTERED (ModuleID),
    CONSTRAINT UQ_Modules_ModuleName UNIQUE (ModuleName)
);
GO

-- 3. ROLE PERMISSIONS 
CREATE TABLE dbo.RolePermissions (
    RoleID          INT             NOT NULL,
    ModuleID        INT             NOT NULL,
    CanView         BIT             NOT NULL CONSTRAINT DF_RolePermissions_CanView   DEFAULT (0),
    CanCreate       BIT             NOT NULL CONSTRAINT DF_RolePermissions_CanCreate DEFAULT (0),
    CanEdit         BIT             NOT NULL CONSTRAINT DF_RolePermissions_CanEdit   DEFAULT (0),
    CanDelete       BIT             NOT NULL CONSTRAINT DF_RolePermissions_CanDelete DEFAULT (0),
    CONSTRAINT PK_RolePermissions PRIMARY KEY CLUSTERED (RoleID, ModuleID),
    CONSTRAINT FK_RolePermissions_Role   FOREIGN KEY (RoleID)   REFERENCES dbo.Roles(RoleID),
    CONSTRAINT FK_RolePermissions_Module FOREIGN KEY (ModuleID) REFERENCES dbo.Modules(ModuleID)
);
GO

-- 4. USERS  
CREATE TABLE dbo.Users (
    UserID           INT IDENTITY(1,1)   NOT NULL,
    FullName         NVARCHAR(100)       NOT NULL,
    Email            NVARCHAR(256)       NOT NULL,
    PasswordHash     NVARCHAR(255)       NOT NULL,   
    RoleID           INT                 NOT NULL,  
    IsActive         BIT                 NOT NULL CONSTRAINT DF_Users_IsActive DEFAULT (1),
    IsArchived       BIT                 NOT NULL CONSTRAINT DF_Users_IsArchived DEFAULT (0),
    LastLoginAt      DATETIME2(0)        NULL,
    CreatedByUserID  INT                 NULL,       
    CreatedAt        DATETIME2(0)        NOT NULL CONSTRAINT DF_Users_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID INT                 NULL,
    ModifiedAt       DATETIME2(0)        NULL,
    RowVersion       ROWVERSION          NOT NULL,
    CONSTRAINT PK_Users PRIMARY KEY CLUSTERED (UserID),
    CONSTRAINT UQ_Users_Email UNIQUE (Email),
    CONSTRAINT FK_Users_Role         FOREIGN KEY (RoleID)           REFERENCES dbo.Roles(RoleID),
    CONSTRAINT FK_Users_CreatedBy    FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Users_ModifiedBy   FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Users_Email CHECK (Email LIKE '%_@__%.__%')
);
GO

-- 5. USER SESSIONS  
CREATE TABLE dbo.UserSessions (
    SessionID       BIGINT IDENTITY(1,1) NOT NULL,
    UserID          INT                  NOT NULL,
    TokenHash       NVARCHAR(255)        NOT NULL,  
    IssuedAt        DATETIME2(0)         NOT NULL CONSTRAINT DF_UserSessions_IssuedAt DEFAULT (SYSUTCDATETIME()),
    ExpiresAt       DATETIME2(0)         NOT NULL,
    RevokedAt       DATETIME2(0)         NULL,
    IPAddress       NVARCHAR(45)         NULL,     
    CONSTRAINT PK_UserSessions PRIMARY KEY CLUSTERED (SessionID),
    CONSTRAINT FK_UserSessions_User FOREIGN KEY (UserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_UserSessions_Expiry CHECK (ExpiresAt > IssuedAt)
);
GO

-- 6. PROJECTS  
CREATE TABLE dbo.Projects (
    ProjectID           INT IDENTITY(1,1) NOT NULL,
    ProjectName         NVARCHAR(150)     NOT NULL,
    Description         NVARCHAR(MAX)     NULL,
    Status              NVARCHAR(20)      NOT NULL CONSTRAINT DF_Projects_Status DEFAULT ('Active'),
    CreatedByUserID     INT               NOT NULL,
    CreatedAt           DATETIME2(0)      NOT NULL CONSTRAINT DF_Projects_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID    INT               NULL,
    ModifiedAt          DATETIME2(0)      NULL,
    ArchivedAt          DATETIME2(0)      NULL,      
    RowVersion          ROWVERSION        NOT NULL,
    CONSTRAINT PK_Projects PRIMARY KEY CLUSTERED (ProjectID),
    CONSTRAINT FK_Projects_CreatedBy  FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Projects_ModifiedBy FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Projects_Status CHECK (Status IN ('Active','Archived'))
);
GO

-- 7. PROJECT MEMBERS  
CREATE TABLE dbo.ProjectMembers (
    ProjectMemberID     INT IDENTITY(1,1) NOT NULL,
    ProjectID           INT               NOT NULL,
    UserID              INT               NOT NULL,
    ProjectRoleID       INT               NOT NULL,   
    AddedByUserID       INT               NOT NULL,  
    JoinedAt            DATETIME2(0)      NOT NULL CONSTRAINT DF_ProjectMembers_JoinedAt DEFAULT (SYSUTCDATETIME()),
    RemovedByUserID     INT               NULL,
    RemovedAt           DATETIME2(0)      NULL,       
    CONSTRAINT PK_ProjectMembers PRIMARY KEY CLUSTERED (ProjectMemberID),
    CONSTRAINT FK_ProjectMembers_Project    FOREIGN KEY (ProjectID)       REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_ProjectMembers_User       FOREIGN KEY (UserID)          REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_ProjectMembers_Role       FOREIGN KEY (ProjectRoleID)   REFERENCES dbo.Roles(RoleID),
    CONSTRAINT FK_ProjectMembers_AddedBy    FOREIGN KEY (AddedByUserID)   REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_ProjectMembers_RemovedBy  FOREIGN KEY (RemovedByUserID) REFERENCES dbo.Users(UserID)
);
GO

CREATE UNIQUE INDEX UQ_ProjectMembers_Active
    ON dbo.ProjectMembers (ProjectID, UserID)
    WHERE RemovedAt IS NULL;
GO

-- 8. REQUIREMENTS  
CREATE TABLE dbo.Requirements (
    RequirementID       INT IDENTITY(1,1) NOT NULL,
    ProjectID           INT               NOT NULL,
    RequirementText     NVARCHAR(MAX)     NOT NULL,
    Status              NVARCHAR(20)      NOT NULL CONSTRAINT DF_Requirements_Status DEFAULT ('Draft'),
    IsArchived          BIT               NOT NULL CONSTRAINT DF_Requirements_IsArchived DEFAULT (0),
    CreatedByUserID     INT               NOT NULL,
    CreatedAt           DATETIME2(0)      NOT NULL CONSTRAINT DF_Requirements_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID    INT               NULL,
    ModifiedAt          DATETIME2(0)      NULL,
    RowVersion          ROWVERSION        NOT NULL,
    CONSTRAINT PK_Requirements PRIMARY KEY CLUSTERED (RequirementID),
    CONSTRAINT FK_Requirements_Project    FOREIGN KEY (ProjectID)        REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_Requirements_CreatedBy  FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Requirements_ModifiedBy FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Requirements_Status CHECK (Status IN ('Draft','UnderReview','Approved','Archived'))
);
GO

-- 9. SPRINTS  
CREATE TABLE dbo.Sprints (
    SprintID            INT IDENTITY(1,1) NOT NULL,
    ProjectID           INT               NOT NULL,
    SprintName          NVARCHAR(100)     NOT NULL,
    StartDate           DATE              NOT NULL,
    EndDate             DATE              NOT NULL,
    Status              NVARCHAR(20)      NOT NULL CONSTRAINT DF_Sprints_Status DEFAULT ('Planned'),
    CreatedByUserID     INT               NOT NULL,
    CreatedAt           DATETIME2(0)      NOT NULL CONSTRAINT DF_Sprints_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID    INT               NULL,
    ModifiedAt          DATETIME2(0)      NULL,
    RowVersion          ROWVERSION        NOT NULL,
    CONSTRAINT PK_Sprints PRIMARY KEY CLUSTERED (SprintID),
    CONSTRAINT FK_Sprints_Project    FOREIGN KEY (ProjectID)        REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_Sprints_CreatedBy  FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Sprints_ModifiedBy FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Sprints_Status CHECK (Status IN ('Planned','Active','Completed','Cancelled')),
    CONSTRAINT CK_Sprints_Dates CHECK (EndDate >= StartDate)
);
GO

-- 10. USER STORIES  
CREATE TABLE dbo.UserStories (
    UserStoryID       INT IDENTITY(1,1) NOT NULL,
    RequirementID     INT               NOT NULL,
    SprintID          INT               NULL,       
    ActorType         NVARCHAR(100)     NULL,       
    Goal              NVARCHAR(500)     NULL,        
    Reason            NVARCHAR(500)     NULL,        
    StoryText         NVARCHAR(MAX)     NOT NULL,     
    Priority          NVARCHAR(10)      NOT NULL CONSTRAINT DF_UserStories_Priority DEFAULT ('Medium'),
    Status            NVARCHAR(20)      NOT NULL CONSTRAINT DF_UserStories_Status DEFAULT ('Draft'),
    IsAIGenerated     BIT               NOT NULL CONSTRAINT DF_UserStories_IsAIGenerated DEFAULT (1),
    AcceptedByUserID  INT               NULL,        
    AcceptedAt        DATETIME2(0)      NULL,
    IsArchived        BIT               NOT NULL CONSTRAINT DF_UserStories_IsArchived DEFAULT (0),
    CreatedByUserID   INT               NOT NULL,
    CreatedAt         DATETIME2(0)      NOT NULL CONSTRAINT DF_UserStories_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID  INT               NULL,
    ModifiedAt        DATETIME2(0)      NULL,
    RowVersion        ROWVERSION        NOT NULL,
    CONSTRAINT PK_UserStories PRIMARY KEY CLUSTERED (UserStoryID),
    CONSTRAINT FK_UserStories_Requirement  FOREIGN KEY (RequirementID)    REFERENCES dbo.Requirements(RequirementID),
    CONSTRAINT FK_UserStories_Sprint       FOREIGN KEY (SprintID)         REFERENCES dbo.Sprints(SprintID),
    CONSTRAINT FK_UserStories_AcceptedBy   FOREIGN KEY (AcceptedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_UserStories_CreatedBy    FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_UserStories_ModifiedBy   FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_UserStories_Priority CHECK (Priority IN ('Low','Medium','High')),
    CONSTRAINT CK_UserStories_Status   CHECK (Status IN ('Draft','Backlog','InSprint','Accepted','Rejected'))
);
GO

-- 11. TASKS  
CREATE TABLE dbo.Tasks (
    TaskID           INT IDENTITY(1,1) NOT NULL,
    ProjectID        INT               NOT NULL,
    UserStoryID      INT               NULL,
    SprintID         INT               NULL,
    Title            NVARCHAR(200)     NOT NULL,
    Description      NVARCHAR(MAX)     NULL,
    AssignedToUserID INT               NULL,
    Status           NVARCHAR(20)      NOT NULL CONSTRAINT DF_Tasks_Status DEFAULT ('ToDo'),
    Priority         NVARCHAR(10)      NOT NULL CONSTRAINT DF_Tasks_Priority DEFAULT ('Medium'),
    DueDate          DATE              NULL,
    IsArchived       BIT               NOT NULL CONSTRAINT DF_Tasks_IsArchived DEFAULT (0),
    CreatedByUserID  INT               NOT NULL,
    CreatedAt        DATETIME2(0)      NOT NULL CONSTRAINT DF_Tasks_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID INT               NULL,
    ModifiedAt       DATETIME2(0)      NULL,
    RowVersion       ROWVERSION        NOT NULL,
    CONSTRAINT PK_Tasks PRIMARY KEY CLUSTERED (TaskID),
    CONSTRAINT FK_Tasks_Project      FOREIGN KEY (ProjectID)        REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_Tasks_UserStory    FOREIGN KEY (UserStoryID)      REFERENCES dbo.UserStories(UserStoryID),
    CONSTRAINT FK_Tasks_Sprint       FOREIGN KEY (SprintID)         REFERENCES dbo.Sprints(SprintID),
    CONSTRAINT FK_Tasks_AssignedTo   FOREIGN KEY (AssignedToUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Tasks_CreatedBy    FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Tasks_ModifiedBy   FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Tasks_Status   CHECK (Status IN ('ToDo','InProgress','InReview','Done','Blocked')),
    CONSTRAINT CK_Tasks_Priority CHECK (Priority IN ('Low','Medium','High'))
);
GO

-- 12. DESIGN SUGGESTIONS  
CREATE TABLE dbo.DesignSuggestions (
    DesignSuggestionID    INT IDENTITY(1,1) NOT NULL,
    ProjectID             INT               NOT NULL,
    RequirementID         INT               NULL,
    Title                 NVARCHAR(200)     NULL,
    ComponentsDescription NVARCHAR(MAX)     NOT NULL,
    IsAIGenerated         BIT               NOT NULL CONSTRAINT DF_DesignSuggestions_IsAIGenerated DEFAULT (1),
    Status                NVARCHAR(20)      NOT NULL CONSTRAINT DF_DesignSuggestions_Status DEFAULT ('Pending'),
    AcceptedByUserID      INT               NULL,
    AcceptedAt            DATETIME2(0)      NULL,
    CreatedByUserID       INT               NOT NULL,
    CreatedAt             DATETIME2(0)      NOT NULL CONSTRAINT DF_DesignSuggestions_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID      INT               NULL,
    ModifiedAt            DATETIME2(0)      NULL,
    CONSTRAINT PK_DesignSuggestions PRIMARY KEY CLUSTERED (DesignSuggestionID),
    CONSTRAINT FK_DesignSuggestions_Project     FOREIGN KEY (ProjectID)        REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_DesignSuggestions_Requirement FOREIGN KEY (RequirementID)    REFERENCES dbo.Requirements(RequirementID),
    CONSTRAINT FK_DesignSuggestions_AcceptedBy  FOREIGN KEY (AcceptedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_DesignSuggestions_CreatedBy   FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_DesignSuggestions_ModifiedBy  FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_DesignSuggestions_Status CHECK (Status IN ('Pending','Accepted','Edited','Rejected'))
);
GO

-- 13. CODE SUGGESTIONS  
CREATE TABLE dbo.CodeSuggestions (
    CodeSuggestionID INT IDENTITY(1,1) NOT NULL,
    TaskID           INT               NOT NULL,
    SuggestedCode    NVARCHAR(MAX)     NOT NULL,
    Language         NVARCHAR(50)      NULL,
    IsAIGenerated    BIT               NOT NULL CONSTRAINT DF_CodeSuggestions_IsAIGenerated DEFAULT (1),
    Status           NVARCHAR(20)      NOT NULL CONSTRAINT DF_CodeSuggestions_Status DEFAULT ('Pending'),
    DecidedByUserID  INT               NULL,     
    DecidedAt        DATETIME2(0)      NULL,
    CreatedAt        DATETIME2(0)      NOT NULL CONSTRAINT DF_CodeSuggestions_CreatedAt DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT PK_CodeSuggestions PRIMARY KEY CLUSTERED (CodeSuggestionID),
    CONSTRAINT FK_CodeSuggestions_Task      FOREIGN KEY (TaskID)          REFERENCES dbo.Tasks(TaskID),
    CONSTRAINT FK_CodeSuggestions_DecidedBy FOREIGN KEY (DecidedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_CodeSuggestions_Status CHECK (Status IN ('Pending','Accepted','Edited','Dismissed'))
);
GO

-- 14. TEST CASES  
CREATE TABLE dbo.TestCases (
    TestCaseID       INT IDENTITY(1,1) NOT NULL,
    ProjectID        INT               NOT NULL,
    UserStoryID      INT               NULL,
    FeatureName      NVARCHAR(200)     NOT NULL,
    Steps            NVARCHAR(MAX)     NOT NULL,
    ExpectedResult   NVARCHAR(MAX)     NOT NULL,
    IsAIGenerated    BIT               NOT NULL CONSTRAINT DF_TestCases_IsAIGenerated DEFAULT (1),
    Status           NVARCHAR(20)      NOT NULL CONSTRAINT DF_TestCases_Status DEFAULT ('Draft'),
    ReviewedByUserID INT               NULL,
    ReviewedAt       DATETIME2(0)      NULL,
    IsArchived       BIT               NOT NULL CONSTRAINT DF_TestCases_IsArchived DEFAULT (0),
    CreatedByUserID  INT               NOT NULL,
    CreatedAt        DATETIME2(0)      NOT NULL CONSTRAINT DF_TestCases_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID INT               NULL,
    ModifiedAt       DATETIME2(0)      NULL,
    RowVersion       ROWVERSION        NOT NULL,
    CONSTRAINT PK_TestCases PRIMARY KEY CLUSTERED (TestCaseID),
    CONSTRAINT FK_TestCases_Project     FOREIGN KEY (ProjectID)        REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_TestCases_UserStory   FOREIGN KEY (UserStoryID)      REFERENCES dbo.UserStories(UserStoryID),
    CONSTRAINT FK_TestCases_ReviewedBy  FOREIGN KEY (ReviewedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_TestCases_CreatedBy   FOREIGN KEY (CreatedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_TestCases_ModifiedBy  FOREIGN KEY (ModifiedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_TestCases_Status CHECK (Status IN ('Draft','Reviewed','Approved','Archived'))
);
GO

-- 15. TEST RUNS  
CREATE TABLE dbo.TestRuns (
    TestRunID        INT IDENTITY(1,1) NOT NULL,
    TestCaseID       INT               NOT NULL,
    ExecutionType    NVARCHAR(10)      NOT NULL,
    Result           NVARCHAR(10)      NOT NULL,
    ExecutedByUserID INT               NULL,    
    Notes            NVARCHAR(MAX)     NULL,
    ExecutedAt       DATETIME2(0)      NOT NULL CONSTRAINT DF_TestRuns_ExecutedAt DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT PK_TestRuns PRIMARY KEY CLUSTERED (TestRunID),
    CONSTRAINT FK_TestRuns_TestCase     FOREIGN KEY (TestCaseID)       REFERENCES dbo.TestCases(TestCaseID),
    CONSTRAINT FK_TestRuns_ExecutedBy   FOREIGN KEY (ExecutedByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_TestRuns_ExecutionType CHECK (ExecutionType IN ('Manual','Automated')),
    CONSTRAINT CK_TestRuns_Result        CHECK (Result IN ('Pass','Fail','Blocked'))
);
GO

-- 16. ISSUES  
CREATE TABLE dbo.Issues (
    IssueID          INT IDENTITY(1,1) NOT NULL,
    ProjectID        INT               NOT NULL,
    Title            NVARCHAR(200)     NOT NULL,
    Description      NVARCHAR(MAX)     NULL,
    Severity         NVARCHAR(10)      NOT NULL,
    Status           NVARCHAR(15)      NOT NULL CONSTRAINT DF_Issues_Status DEFAULT ('Open'),
    ReportedByUserID INT               NOT NULL,
    AssignedToUserID INT               NULL,
    RelatedTestRunID INT               NULL,
    RelatedTaskID    INT               NULL,
    IsArchived       BIT               NOT NULL CONSTRAINT DF_Issues_IsArchived DEFAULT (0),
    CreatedAt        DATETIME2(0)      NOT NULL CONSTRAINT DF_Issues_CreatedAt DEFAULT (SYSUTCDATETIME()),
    ModifiedByUserID INT               NULL,
    ModifiedAt       DATETIME2(0)      NULL,
    ClosedByUserID   INT               NULL,
    ClosedAt         DATETIME2(0)      NULL,
    RowVersion       ROWVERSION        NOT NULL,
    CONSTRAINT PK_Issues PRIMARY KEY CLUSTERED (IssueID),
    CONSTRAINT FK_Issues_Project      FOREIGN KEY (ProjectID)         REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_Issues_ReportedBy   FOREIGN KEY (ReportedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Issues_AssignedTo   FOREIGN KEY (AssignedToUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Issues_TestRun      FOREIGN KEY (RelatedTestRunID)  REFERENCES dbo.TestRuns(TestRunID),
    CONSTRAINT FK_Issues_Task         FOREIGN KEY (RelatedTaskID)     REFERENCES dbo.Tasks(TaskID),
    CONSTRAINT FK_Issues_ModifiedBy   FOREIGN KEY (ModifiedByUserID)  REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Issues_ClosedBy     FOREIGN KEY (ClosedByUserID)    REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Issues_Severity CHECK (Severity IN ('Low','Medium','High')),
    CONSTRAINT CK_Issues_Status   CHECK (Status IN ('Open','InProgress','Closed')),
    CONSTRAINT CK_Issues_ClosedConsistency CHECK (
        (Status = 'Closed' AND ClosedAt IS NOT NULL AND ClosedByUserID IS NOT NULL)
        OR (Status <> 'Closed')
    )
);
GO

-- 17. DEPLOYMENTS  
CREATE TABLE dbo.Deployments (
    DeploymentID      INT IDENTITY(1,1) NOT NULL,
    ProjectID         INT               NOT NULL,
    Version           NVARCHAR(50)      NOT NULL,
    Status            NVARCHAR(15)      NOT NULL CONSTRAINT DF_Deployments_Status DEFAULT ('InProgress'),
    TriggeredByUserID INT               NOT NULL,
    StartedAt         DATETIME2(0)      NOT NULL CONSTRAINT DF_Deployments_StartedAt DEFAULT (SYSUTCDATETIME()),
    CompletedAt       DATETIME2(0)      NULL,
    ErrorLogURL       NVARCHAR(500)     NULL,   
    RowVersion        ROWVERSION        NOT NULL,
    CONSTRAINT PK_Deployments PRIMARY KEY CLUSTERED (DeploymentID),
    CONSTRAINT FK_Deployments_Project      FOREIGN KEY (ProjectID)         REFERENCES dbo.Projects(ProjectID),
    CONSTRAINT FK_Deployments_TriggeredBy  FOREIGN KEY (TriggeredByUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT CK_Deployments_Status CHECK (Status IN ('InProgress','Successful','Failed')),
    CONSTRAINT CK_Deployments_Completion CHECK (CompletedAt IS NULL OR CompletedAt >= StartedAt)
);
GO

-- 18. NOTIFICATIONS 
CREATE TABLE dbo.Notifications (
    NotificationID   INT IDENTITY(1,1) NOT NULL,
    RecipientUserID  INT               NOT NULL,
    DeploymentID     INT               NULL,
    IssueID          INT               NULL,
    Message          NVARCHAR(500)     NOT NULL,
    NotificationType NVARCHAR(30)      NOT NULL,
    IsRead           BIT               NOT NULL CONSTRAINT DF_Notifications_IsRead DEFAULT (0),
    CreatedAt        DATETIME2(0)      NOT NULL CONSTRAINT DF_Notifications_CreatedAt DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT PK_Notifications PRIMARY KEY CLUSTERED (NotificationID),
    CONSTRAINT FK_Notifications_Recipient FOREIGN KEY (RecipientUserID) REFERENCES dbo.Users(UserID),
    CONSTRAINT FK_Notifications_Deployment FOREIGN KEY (DeploymentID)  REFERENCES dbo.Deployments(DeploymentID),
    CONSTRAINT FK_Notifications_Issue      FOREIGN KEY (IssueID)       REFERENCES dbo.Issues(IssueID),
    CONSTRAINT CK_Notifications_Type CHECK (NotificationType IN ('DeploymentSuccess','DeploymentFailure','IssueAssigned','General'))
);
GO

-- 19. AUDIT LOG  
CREATE TABLE dbo.AuditLog (
    AuditLogID BIGINT IDENTITY(1,1) NOT NULL,
    UserID     INT                  NULL,   
    Action     NVARCHAR(100)        NOT NULL,
    EntityType NVARCHAR(50)         NOT NULL,
    EntityID   INT                  NULL,
    Details    NVARCHAR(MAX)        NULL,
    IPAddress  NVARCHAR(45)         NULL,
    CreatedAt  DATETIME2(0)         NOT NULL CONSTRAINT DF_AuditLog_CreatedAt DEFAULT (SYSUTCDATETIME()),
    CONSTRAINT PK_AuditLog PRIMARY KEY CLUSTERED (AuditLogID),
    CONSTRAINT FK_AuditLog_User FOREIGN KEY (UserID) REFERENCES dbo.Users(UserID)
);
GO

-- 20. INDEXES  
CREATE INDEX IX_Users_Role                    ON dbo.Users (RoleID);
CREATE INDEX IX_UserSessions_User             ON dbo.UserSessions (UserID);
CREATE INDEX IX_ProjectMembers_User           ON dbo.ProjectMembers (UserID);
CREATE INDEX IX_Requirements_Project          ON dbo.Requirements (ProjectID, Status);
CREATE INDEX IX_Sprints_Project               ON dbo.Sprints (ProjectID, Status);
CREATE INDEX IX_UserStories_Requirement       ON dbo.UserStories (RequirementID);
CREATE INDEX IX_UserStories_Sprint            ON dbo.UserStories (SprintID, Status);
CREATE INDEX IX_Tasks_Project_Status          ON dbo.Tasks (ProjectID, Status);
CREATE INDEX IX_Tasks_AssignedTo              ON dbo.Tasks (AssignedToUserID, Status);
CREATE INDEX IX_Tasks_Sprint                  ON dbo.Tasks (SprintID);
CREATE INDEX IX_DesignSuggestions_Project     ON dbo.DesignSuggestions (ProjectID);
CREATE INDEX IX_CodeSuggestions_Task          ON dbo.CodeSuggestions (TaskID);
CREATE INDEX IX_TestCases_Project             ON dbo.TestCases (ProjectID, Status);
CREATE INDEX IX_TestCases_UserStory           ON dbo.TestCases (UserStoryID);
CREATE INDEX IX_TestRuns_TestCase_Date        ON dbo.TestRuns (TestCaseID, ExecutedAt DESC);
CREATE INDEX IX_Issues_Project_Status         ON dbo.Issues (ProjectID, Status, Severity);
CREATE INDEX IX_Issues_AssignedTo             ON dbo.Issues (AssignedToUserID, Status);
CREATE INDEX IX_Deployments_Project_Date      ON dbo.Deployments (ProjectID, StartedAt DESC);
CREATE INDEX IX_Notifications_Recipient       ON dbo.Notifications (RecipientUserID, IsRead);
CREATE INDEX IX_AuditLog_Entity               ON dbo.AuditLog (EntityType, EntityID);
CREATE INDEX IX_AuditLog_User_Date            ON dbo.AuditLog (UserID, CreatedAt DESC);
GO