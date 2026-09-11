// ============================================
// ISSUE REPORT GENERATOR (frontend only)
// ============================================

function generateRefId(issueId) {
  const year = new Date().getFullYear();
  const num  = String(issueId).padStart(6, "0");
  return `CF-${year}-${num}`;
}

function escapeHtml(s) {
  return String(s)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

function buildReportHTML(issue, reporter) {
  const refId = generateRefId(issue.id);
  const submitted = new Date(issue.createdAt).toLocaleString();

  return `<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Issue Report ${refId}</title>
<style>
  * { box-sizing: border-box; font-family: 'Segoe UI', system-ui, sans-serif; }
  body { background: #fff; color: #111; padding: 40px; max-width: 820px; margin: auto; }
  .header { display: flex; justify-content: space-between; align-items: flex-start;
            border-bottom: 3px solid #1e3a8a; padding-bottom: 16px; margin-bottom: 24px; }
  .header h1 { margin: 0; color: #1e3a8a; font-size: 24px; }
  .header .sub { color: #555; font-size: 13px; margin-top: 4px; }
  .ref { text-align: right; }
  .ref strong { display: block; font-size: 20px; color: #1e3a8a; letter-spacing: 1px; }
  .ref small { color: #666; }
  .section { margin: 20px 0; }
  .section h2 { font-size: 13px; text-transform: uppercase; letter-spacing: 1px;
                color: #1e3a8a; border-bottom: 1px solid #e5e7eb;
                padding-bottom: 6px; margin-bottom: 10px; }
  .row { display: flex; padding: 8px 0; border-bottom: 1px dashed #eef1f6; font-size: 14px; }
  .row .label { width: 180px; font-weight: 600; color: #374151; }
  .row .value { flex: 1; color: #111; white-space: pre-wrap; }
  .badge { display: inline-block; padding: 3px 10px; border-radius: 20px;
           background: #e5e7eb; font-size: 12px; font-weight: 600; }
  .badge.high   { background: #fee2e2; color: #991b1b; }
  .badge.medium { background: #fef3c7; color: #92400e; }
  .badge.low    { background: #e0f2fe; color: #075985; }
  .footer { margin-top: 40px; padding-top: 16px; border-top: 1px solid #e5e7eb;
            font-size: 12px; color: #666; text-align: center; }
  .stamp { color: #059669; font-weight: bold; }
</style>
</head>
<body>

  <div class="header">
    <div>
      <h1>🏛️ Campus Fix</h1>
      <div class="sub">Official Issue Report — Proof of Submission</div>
    </div>
    <div class="ref">
      <small>Reference ID</small>
      <strong>${refId}</strong>
      <small>Submitted: ${submitted}</small>
    </div>
  </div>

  <div class="section">
    <h2>Reporter Details</h2>
    <div class="row"><div class="label">Name</div><div class="value">${escapeHtml(reporter.name)}</div></div>
    <div class="row"><div class="label">Email</div><div class="value">${escapeHtml(reporter.email)}</div></div>
    <div class="row"><div class="label">Role</div><div class="value">${escapeHtml(reporter.role)}</div></div>
  </div>

  <div class="section">
    <h2>Issue Details</h2>
    <div class="row"><div class="label">Title</div><div class="value">${escapeHtml(issue.title)}</div></div>
    <div class="row"><div class="label">Category</div><div class="value">${escapeHtml(issue.category)}</div></div>
    <div class="row"><div class="label">Location</div><div class="value">${escapeHtml(issue.location)}</div></div>
    <div class="row"><div class="label">Priority</div><div class="value"><span class="badge ${issue.priority.toLowerCase()}">${issue.priority}</span></div></div>
    <div class="row"><div class="label">Status</div><div class="value">${issue.status}</div></div>
    <div class="row"><div class="label">Description</div><div class="value">${escapeHtml(issue.description)}</div></div>
  </div>

  <div class="footer">
    This is a system-generated report. Retain it for your records.<br>
    Status confirmed: <span class="stamp">RECEIVED</span><br>
    Campus Fix — Smart Campus Maintenance System
  </div>

</body>
</html>`;
}

function showReportModal(issue, reporter) {
  const html = buildReportHTML(issue, reporter);
  document.getElementById("cfReportModal")?.remove();

  const modal = document.createElement("div");
  modal.id = "cfReportModal";
  modal.className = "cf-modal-backdrop";
  modal.innerHTML = `
    <div class="cf-modal">
      <div class="cf-modal-head">
        <h3>📄 Issue Report Generated</h3>
        <button class="cf-modal-close" title="Close">✕</button>
      </div>
      <div class="cf-modal-body">
        <iframe id="cfReportFrame" style="width:100%; height:500px; border:none;"></iframe>
      </div>
      <div class="cf-modal-foot">
        <button class="btn btn-primary" id="cfPrintBtn">🖨️ Print / Save as PDF</button>
        <button class="btn btn-ok" id="cfDownloadBtn">⬇️ Download</button>
      </div>
    </div>
  `;
  document.body.appendChild(modal);

  const frame = document.getElementById("cfReportFrame");
  frame.srcdoc = html;

  modal.querySelector(".cf-modal-close").onclick = () => modal.remove();
  modal.addEventListener("click", e => { if (e.target === modal) modal.remove(); });

  document.getElementById("cfPrintBtn").onclick = () => {
    frame.contentWindow.focus();
    frame.contentWindow.print();
  };

  document.getElementById("cfDownloadBtn").onclick = () => {
    const refId = generateRefId(issue.id);
    const blob = new Blob([html], { type: "text/html;charset=utf-8" });
    const url  = URL.createObjectURL(blob);
    const a    = document.createElement("a");
    a.href = url;
    a.download = `CampusFix_Report_${refId}.html`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };
}