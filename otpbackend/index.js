const admin = require("firebase-admin");
const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { Resend } = require("resend");

admin.initializeApp();
const db = admin.firestore();
const resend = new Resend("re_SW2mvojM_CzfqQtNUvzbkG7xw8923NiJa");

// ── Enviar OTP ──────────────────────────────────────
exports.enviarOtpCorreo = onCall(async (request) => {
    const email = request.data.email;
    console.log("EMAIL RECIBIDO:", email);

    if (!email) {
        throw new HttpsError("invalid-argument", "Email requerido");
    }

    const codigo = Math.floor(100000 + Math.random() * 900000).toString();
    const expira = Date.now() + 5 * 60 * 1000;

    await db.collection("email_otps").doc(email).set({
        codigo,
        expira,
        creado: Date.now()
    });

    await resend.emails.send({
        from: "Rebuska <noreply@rebuska.online>",
        to: email,
        subject: "Código de verificación",
        html: `
        <!DOCTYPE html>
        <html>
        <head><meta charset="UTF-8"></head>
        <body style="margin:0;padding:0;background:#F0F4FF;font-family:Arial,sans-serif;">

          <table width="100%" cellpadding="0" cellspacing="0" style="background:#F0F4FF;padding:40px 0;">
            <tr><td align="center">
              <table width="480" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.08);">

                <!-- HEADER AZUL -->
                <tr>
                  <td style="background:linear-gradient(135deg,#1565C0,#1D4ED8);padding:36px 40px;text-align:center;">
                    <h1 style="margin:0;color:#ffffff;font-size:28px;font-weight:900;letter-spacing:1px;">Rebuska</h1>
                    <p style="margin:6px 0 0;color:rgba(255,255,255,0.8);font-size:13px;">Conectando servicios y personas</p>
                  </td>
                </tr>

                <!-- CONTENIDO -->
                <tr>
                  <td style="padding:40px;">
                    <p style="margin:0 0 8px;font-size:22px;font-weight:800;color:#1a1a2e;">Verifica tu correo</p>
                    <p style="margin:0 0 28px;font-size:14px;color:#6b7280;line-height:1.6;">
                      Usa el siguiente código para completar tu registro en Rebuska.
                      Este código es de un solo uso y expira en <strong>5 minutos</strong>.
                    </p>

                    <!-- CÓDIGO -->
                    <div style="background:#EFF6FF;border:2px dashed #1D4ED8;border-radius:12px;padding:28px;text-align:center;margin-bottom:28px;">
                      <p style="margin:0 0 6px;font-size:12px;color:#6b7280;text-transform:uppercase;letter-spacing:2px;">Tu código</p>
                      <p style="margin:0;font-size:42px;font-weight:900;letter-spacing:12px;color:#1D4ED8;">${codigo}</p>
                    </div>

                    <!-- ADVERTENCIA -->
                    <div style="background:#FFFBEB;border-left:4px solid #F59E0B;border-radius:6px;padding:14px 16px;margin-bottom:28px;">
                      <p style="margin:0;font-size:13px;color:#92400E;">
                        🔒 <strong>Nunca compartas este código.</strong> Rebuska jamás te lo pedirá por otro medio.
                      </p>
                    </div>

                    <p style="margin:0;font-size:13px;color:#9ca3af;">
                      Si no solicitaste este código, puedes ignorar este correo con seguridad.
                    </p>
                  </td>
                </tr>

                <!-- FOOTER -->
                <tr>
                  <td style="background:#F8FAFF;padding:20px 40px;text-align:center;border-top:1px solid #E5E7EB;">
                    <p style="margin:0;font-size:12px;color:#9ca3af;">
                      © 2026 Rebuska ·
                      <a href="mailto:soporte@rebuska.online" style="color:#1D4ED8;text-decoration:none;">soporte@rebuska.online</a>
                    </p>
                  </td>
                </tr>

              </table>
            </td></tr>
          </table>

        </body>
        </html>
        `
    });

    return { success: true };
});

// ── Verificar OTP ───────────────────────────────────
exports.verificarOtpCorreo = onCall(async (request) => {
    const email  = request.data.email;
    const codigo = request.data.codigo;

    if (!email || !codigo) {
        throw new HttpsError("invalid-argument", "Datos incompletos");
    }

    const doc = await db.collection("email_otps").doc(email).get();

    if (!doc.exists) {
        throw new HttpsError("not-found", "Código no encontrado");
    }

    const otpData = doc.data();

    if (Date.now() > otpData.expira) {
        throw new HttpsError("deadline-exceeded", "Código expirado");
    }

    if (otpData.codigo !== codigo) {
        throw new HttpsError("invalid-argument", "Código incorrecto");
    }

    const user = await admin.auth().getUserByEmail(email);
    await admin.auth().updateUser(user.uid, { emailVerified: true });
    await db.collection("email_otps").doc(email).delete();

    return { success: true };
});