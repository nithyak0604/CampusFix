// ============= DASHBOARDS =============

function getUserById(id) {
  return DB.getUsers().find(u => u.id === id) || { name: "—" };
}

// ---------- STUDENT / FACULTY ----------
function renderStudentDashboard() {
  const user = requireLogin(["student", "faculty"]);
  if (!user) return;

  const issues = DB.getIssues()
    .filter(i => i.reporterId === user.id)
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

  const container = document.getElementById("issueList");

  if (!issues.length) {
    container.innerHTML = `<p class="empty">No issues reported yet.</p>`;
    return;
  }

  container.innerHTML = `
    <table class="data-table">
      <thead>
        <tr>
          <th>#</th><th>Title</th><th>Category</th><th>Priority</th>
          <th>Status</th><th>Updated</th><th>Action</th>
        </tr>
      </thead>
      <tbody>
        ${issues.map(i => `
          <tr>
            <td>#${i.id}</td>
            <td><strong>${i.title}</strong><br><small>${i.location}</small></td>
            <td>${i.category}</td>
            <td><span class="prio ${i.priority.toLowerCase()}">${i.priority}</span></td>
            <td><span class="${statusClass(i.status)}">${i.status}</span></td>
            <td>${fmtDate(i.updatedAt)}</td>
            <td>${studentActions(i)}</td>
          </tr>
        `).join("")}
      </tbody>
    </table>
  `;

  container.querySelectorAll("form[data-issue]").forEach(f => {
    f.addEventListener("submit", e => {
      e.preventDefault();
      const id = Number(f.dataset.issue);
      handleStudentAction(id, e.submitter.value, f.remarks.value);
    });
  });
}

function studentActions(issue) {
  if (issue.status === "Verified") {
    return `
      <form data-issue="${issue.id}">
        <textarea name="remarks" rows="2" placeholder="Optional remarks"></textarea>
        <div class="row">
          <button name="action" value="close" class="btn btn-ok small">✔ Confirm</button>
          <button name="action" value="reopen" class="btn btn-warn small">↺ Reopen</button>
        </div>
      </form>`;
  }
  if (issue.status === "Closed") {
    return `<em class="muted">Closed — "${issue.userFeedback || '—'}"</em>`;
  }
  return `<em class="muted">In progress…</em>`;
}

function handleStudentAction(id, action, remarks) {
  const issues = DB.getIssues();
  const issue = issues.find(i => i.id === id);
  if (!issue) return;

  issue.userFeedback = remarks.trim();
  issue.updatedAt = new Date().toISOString();

  if (action === "close") {
    issue.status = "Closed";
  } else {
    issue.status = "Reopened";
    issue.verifierId = null;
    issue.verifierRemarks = "";
  }
  DB.saveIssues(issues);
  renderStudentDashboard();
}

// ---------- REPORT ----------
const reportForm = document.getElementById("reportForm");
if (reportForm) {
  const user = requireLogin(["student", "faculty"]);
  if (user) {
    reportForm.addEventListener("submit", e => {
      e.preventDefault();
      const form = e.target;
      const issues = DB.getIssues();
      const issue = {
        id: DB.nextIssueId(),
        title: form.title.value.trim(),
        description: form.description.value.trim(),
        category: form.category.value,
        location: form.location.value.trim(),
        priority: form.priority.value,
        status: "Reported",
        reporterId: user.id,
        assignedTo: null,
        verifierId: null,
        staffRemarks: "",
        verifierRemarks: "",
        userFeedback: "",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
            issues.push(issue);
      DB.saveIssues(issues);

      // Show the generated report document as proof
      showReportModal(issue, user);

      // Return to dashboard when modal closes
      const observer = new MutationObserver(() => {
        if (!document.getElementById("cfReportModal")) {
          observer.disconnect();
          window.location.href = "student.html";
        }
      });
      observer.observe(document.body, { childList: true });
    });
  }
}

// ---------- ADMIN ----------
function renderAdminDashboard() {
  const user = requireLogin(["admin"]);
  if (!user) return;

  const issues = DB.getIssues().sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  const staffList = DB.getUsers().filter(u => u.role === "staff");
  const verifierList = DB.getUsers().filter(u => u.role === "verifier");

  const statsBar = document.getElementById("statsBar");
  const counts = {
    Total: issues.length,
    Reported: issues.filter(i => i.status === "Reported").length,
    Assigned: issues.filter(i => i.status === "Assigned").length,
    Resolved: issues.filter(i => i.status === "Resolved").length,
    Verified: issues.filter(i => i.status === "Verified").length,
    Closed: issues.filter(i => i.status === "Closed").length,
    Reopened: issues.filter(i => i.status === "Reopened").length,
  };
  statsBar.innerHTML = Object.entries(counts)
    .map(([k, v]) => `<div class="stat"><span>${v}</span><label>${k}</label></div>`)
    .join("");

  const container = document.getElementById("adminList");

  container.innerHTML = `
    <table class="data-table">
      <thead>
        <tr>
          <th>#</th><th>Title</th><th>Reporter</th><th>Priority</th>
          <th>Status</th><th>Assign Staff</th><th>Assign Verifier</th>
        </tr>
      </thead>
      <tbody>
        ${issues.map(i => `
          <tr>
            <td>#${i.id}</td>
            <td><strong>${i.title}</strong><br><small>${i.location}</small></td>
            <td>${getUserById(i.reporterId).name}</td>
            <td><span class="prio ${i.priority.toLowerCase()}">${i.priority}</span></td>
            <td><span class="${statusClass(i.status)}">${i.status}</span></td>
            <td>${assignStaffCell(i, staffList)}</td>
            <td>${assignVerifierCell(i, verifierList)}</td>
          </tr>
        `).join("")}
      </tbody>
    </table>
  `;

  container.querySelectorAll("form[data-assign]").forEach(f => {
    f.addEventListener("submit", e => {
      e.preventDefault();
      const id = Number(f.dataset.assign);
      const staffId = Number(f.staffId.value);
      adminAssignStaff(id, staffId);
    });
  });

  container.querySelectorAll("form[data-verify]").forEach(f => {
    f.addEventListener("submit", e => {
      e.preventDefault();
      const id = Number(f.dataset.verify);
      const verifierId = Number(f.verifierId.value);
      adminAssignVerifier(id, verifierId);
    });
  });
}

function assignStaffCell(issue, staffList) {
  if (issue.status === "Reported" || issue.status === "Reopened") {
    return `
      <form data-assign="${issue.id}">
        <select name="staffId" required>
          <option value="">-- choose --</option>
          ${staffList.map(s => `<option value="${s.id}">${s.name}</option>`).join("")}
        </select>
        <button class="btn btn-primary small">Assign</button>
      </form>`;
  }
  return `<em class="muted">${issue.assignedTo ? getUserById(issue.assignedTo).name : "—"}</em>`;
}

function assignVerifierCell(issue, verifierList) {
  if (issue.status === "Resolved") {
    return `
      <form data-verify="${issue.id}">
        <select name="verifierId" required>
          <option value="">-- choose --</option>
          ${verifierList.map(v => `<option value="${v.id}">${v.name}</option>`).join("")}
        </select>
        <button class="btn btn-primary small">Send</button>
      </form>`;
  }
  return `<em class="muted">${issue.verifierId ? getUserById(issue.verifierId).name : "—"}</em>`;
}

function adminAssignStaff(id, staffId) {
  const issues = DB.getIssues();
  const issue = issues.find(i => i.id === id);
  issue.assignedTo = staffId;
  issue.status = "Assigned";
  issue.updatedAt = new Date().toISOString();
  DB.saveIssues(issues);
  renderAdminDashboard();
}

function adminAssignVerifier(id, verifierId) {
  const issues = DB.getIssues();
  const issue = issues.find(i => i.id === id);
  issue.verifierId = verifierId;
  issue.status = "Pending Verification";
  issue.updatedAt = new Date().toISOString();
  DB.saveIssues(issues);
  renderAdminDashboard();
}

// ---------- STAFF ----------
function renderStaffDashboard() {
  const user = requireLogin(["staff"]);
  if (!user) return;

  const issues = DB.getIssues()
    .filter(i => i.assignedTo === user.id)
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

  const container = document.getElementById("staffList");
  if (!issues.length) {
    container.innerHTML = `<p class="empty">No issues assigned yet.</p>`;
    return;
  }

  container.innerHTML = `
    <table class="data-table">
      <thead>
        <tr><th>#</th><th>Issue</th><th>Location</th><th>Priority</th><th>Status</th><th>Action</th></tr>
      </thead>
      <tbody>
        ${issues.map(i => `
          <tr>
            <td>#${i.id}</td>
            <td><strong>${i.title}</strong><br><small>${i.description}</small></td>
            <td>${i.location}</td>
            <td><span class="prio ${i.priority.toLowerCase()}">${i.priority}</span></td>
            <td><span class="${statusClass(i.status)}">${i.status}</span></td>
            <td>${staffActionCell(i)}</td>
          </tr>
        `).join("")}
      </tbody>
    </table>
  `;

  container.querySelectorAll("form[data-resolve]").forEach(f => {
    f.addEventListener("submit", e => {
      e.preventDefault();
      const id = Number(f.dataset.resolve);
      handleResolve(id, f.remarks.value);
    });
  });
}

function staffActionCell(issue) {
  if (issue.status === "Assigned" || issue.status === "Reopened") {
    return `
      <form data-resolve="${issue.id}">
        <textarea name="remarks" rows="2" placeholder="Work done / notes" required></textarea>
        <button class="btn btn-ok small">Mark Resolved</button>
      </form>`;
  }
  return `<em class="muted">${issue.staffRemarks || "—"}</em>`;
}

function handleResolve(id, remarks) {
  const issues = DB.getIssues();
  const issue = issues.find(i => i.id === id);
  issue.staffRemarks = remarks.trim();
  issue.status = "Resolved";
  issue.updatedAt = new Date().toISOString();
  DB.saveIssues(issues);
  renderStaffDashboard();
}

// ---------- VERIFIER ----------
function renderVerifierDashboard() {
  const user = requireLogin(["verifier"]);
  if (!user) return;

  const issues = DB.getIssues()
    .filter(i => i.verifierId === user.id)
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

  const container = document.getElementById("verifierList");
  if (!issues.length) {
    container.innerHTML = `<p class="empty">No issues assigned for verification.</p>`;
    return;
  }

  container.innerHTML = `
    <table class="data-table">
      <thead>
        <tr><th>#</th><th>Issue</th><th>Staff Remarks</th><th>Status</th><th>Verify</th></tr>
      </thead>
      <tbody>
        ${issues.map(i => `
          <tr>
            <td>#${i.id}</td>
            <td><strong>${i.title}</strong><br><small>${i.location}</small></td>
            <td>${i.staffRemarks || "—"}</td>
            <td><span class="${statusClass(i.status)}">${i.status}</span></td>
            <td>${verifierActionCell(i)}</td>
          </tr>
        `).join("")}
      </tbody>
    </table>
  `;

  container.querySelectorAll("form[data-check]").forEach(f => {
    f.addEventListener("submit", e => {
      e.preventDefault();
      const id = Number(f.dataset.check);
      handleVerify(id, e.submitter.value, f.remarks.value);
    });
  });
}

function verifierActionCell(issue) {
  if (issue.status === "Pending Verification") {
    return `
      <form data-check="${issue.id}">
        <textarea name="remarks" rows="2" placeholder="Inspection notes" required></textarea>
        <div class="row">
          <button name="action" value="pass" class="btn btn-ok small">✔ Pass</button>
          <button name="action" value="fail" class="btn btn-warn small">✘ Reject</button>
        </div>
      </form>`;
  }
  return `<em class="muted">${issue.verifierRemarks || "—"}</em>`;
}

function handleVerify(id, action, remarks) {
  const issues = DB.getIssues();
  const issue = issues.find(i => i.id === id);
  issue.verifierRemarks = remarks.trim();
  issue.status = action === "pass" ? "Verified" : "Assigned";
  issue.updatedAt = new Date().toISOString();
  DB.saveIssues(issues);
  renderVerifierDashboard();
}