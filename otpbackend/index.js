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
            <div style="font-family: Arial">
                <h2>Verifica tu correo</h2>
                <p>Tu código de verificación es:</p>
                <div style="font-size: 32px; font-weight: bold;
                            letter-spacing: 8px; color: #1D4ED8;">
                    ${codigo}
                </div>
                <p>Ingresa este código en la App.</p>
                <p>Este código expira en 5 minutos.</p>
            </div>
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