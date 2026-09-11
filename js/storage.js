// ============= CAMPUS FIX — LocalStorage DB =============

const DB = {
  // ---- users ----
  getUsers() {
    return JSON.parse(localStorage.getItem("cf_users") || "[]");
  },
  saveUsers(users) {
    localStorage.setItem("cf_users", JSON.stringify(users));
  },

  // ---- issues ----
  getIssues() {
    return JSON.parse(localStorage.getItem("cf_issues") || "[]");
  },
  saveIssues(issues) {
    localStorage.setItem("cf_issues", JSON.stringify(issues));
  },

  // ---- session ----
  getCurrentUser() {
    return JSON.parse(localStorage.getItem("cf_current") || "null");
  },
  setCurrentUser(user) {
    localStorage.setItem("cf_current", JSON.stringify(user));
  },
  clearCurrentUser() {
    localStorage.removeItem("cf_current");
  },

  nextIssueId() {
    const issues = DB.getIssues();
    return issues.length ? Math.max(...issues.map(i => i.id)) + 1 : 1;
  },
};

// ---------- Seed demo data once ----------
(function seedOnce() {
  if (localStorage.getItem("cf_seeded")) return;

  const demoUsers = [
    { id: 1, name: "Admin One",      email: "admin@campus.edu",   password: "admin123",    role: "admin"    },
    { id: 2, name: "Ravi (Student)", email: "ravi@campus.edu",    password: "student123",  role: "student"  },
    { id: 3, name: "Dr. Meera",      email: "meera@campus.edu",   password: "faculty123",  role: "faculty"  },
    { id: 4, name: "Suresh (Staff)", email: "suresh@campus.edu",  password: "staff123",    role: "staff"    },
    { id: 5, name: "Anita (RVR)",    email: "anita@campus.edu",   password: "verifier123", role: "verifier" },
  ];
  DB.saveUsers(demoUsers);

  const demoIssues = [
    {
      id: 1,
      title: "Broken tube light in Lab 204",
      description: "Second tube light from the door is dead.",
      category: "Electrical",
      location: "Block B, Lab 204",
      priority: "High",
      status: "Reported",
      reporterId: 2,
      assignedTo: null,
      verifierId: null,
      staffRemarks: "",
      verifierRemarks: "",
      userFeedback: "",
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    },
  ];
  DB.saveIssues(demoIssues);

  localStorage.setItem("cf_seeded", "1");
})();