// ============= AUTH =============
function goToDashboard(role) {
  const map = {
    student:  "pages/student.html",
    faculty:  "pages/student.html",
    admin:    "pages/admin.html",
    staff:    "pages/staff.html",
    verifier: "pages/verifier.html",
  };
  window.location.href = map[role] || "login.html";
}

// ---- Login ----
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", e => {
    e.preventDefault();
    const email = e.target.email.value.trim().toLowerCase();
    const password = e.target.password.value;

    const user = DB.getUsers().find(u => u.email === email && u.password === password);
    if (!user) {
      alert("Invalid credentials");
      return;
    }
    DB.setCurrentUser(user);
    goToDashboard(user.role);
  });
}

// ---- Register ----
const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", e => {
    e.preventDefault();
    const form = e.target;
    const email = form.email.value.trim().toLowerCase();

    if (DB.getUsers().some(u => u.email === email)) {
      alert("Email already registered");
      return;
    }

    const users = DB.getUsers();
    const newUser = {
      id: users.length ? Math.max(...users.map(u => u.id)) + 1 : 1,
      name: form.name.value.trim(),
      email,
      password: form.password.value,
      role: form.role.value,
    };
    users.push(newUser);
    DB.saveUsers(users);

    alert("Registered! Please log in.");
    window.location.href = "login.html";
  });
}