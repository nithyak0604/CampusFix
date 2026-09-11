// ============= SHARED HELPERS =============

function requireLogin(allowedRoles) {
  const user = DB.getCurrentUser();
  if (!user) {
    window.location.href = "../login.html";
    return null;
  }
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    alert("Unauthorized");
    window.location.href = "../login.html";
    return null;
  }
  return user;
}

function mountNavbar() {
  const user = DB.getCurrentUser();
  const info = document.getElementById("userInfo");
  if (info && user) {
    info.textContent = `👤 ${user.name} (${user.role})`;
  }
  const logout = document.getElementById("logoutBtn");
  if (logout) {
    logout.addEventListener("click", e => {
      e.preventDefault();
      DB.clearCurrentUser();
      window.location.href = "../login.html";
    });
  }
}

function statusClass(status) {
  return "badge " + status.toLowerCase().replace(/\s+/g, "-");
}

function fmtDate(iso) {
  const d = new Date(iso);
  return d.toLocaleString([], { month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" });
}

document.addEventListener("DOMContentLoaded", mountNavbar);