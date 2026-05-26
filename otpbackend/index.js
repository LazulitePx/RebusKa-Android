const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { Resend } = require("resend");

admin.initializeApp();

const db = admin.firestore();

const resend = new Resend("re_SW2mvojM_CzfqQtNUvzbkG7xw8923NiJa");

// Función enviar código OTP al correo
exports.enviarOtpCorreo = functions.https.onCall(
  async (data, context) => {

    const email = data.email;

    if (!email) {
      throw new functions.https.HttpsError(
        "invalid-argument",
        "Email requerido"
      );
    }

    // Generar OTP
    const codigo = Math.floor(
      100000 + Math.random() * 900000
    ).toString();

    // Expira en 5 minutos
    const expira = Date.now() + 5 * 60 * 1000;

    // Guardar OTP en Firestore
    await db.collection("email_otps")
      .doc(email)
      .set({
        codigo,
        expira,
        creado: Date.now()
      });

    // Enviar correo
    await resend.emails.send({
      from: "Rebuska <onboarding@resend.dev>",
      to: email,
      subject: "Código de verificación",
      html: `
        <div style="font-family: Arial">

          <h2>Verifica tu correo</h2>

          <p>Tu código OTP es:</p>

          <div style="
            font-size: 32px;
            font-weight: bold;
            letter-spacing: 8px;
            color: #1D4ED8;
          ">
            ${codigo}
          </div>

          <p>
            Este código expira en 5 minutos.
          </p>

        </div>
      `
    });

    return {
      success: true
    };
  }
)

// Función verificar código OTP
exports.verificarOtpCorreo = functions.https.onCall(
  async (data, context) => {

    const email = data.email;
    const codigo = data.codigo;

    // Validaciones
    if (!email || !codigo) {

      throw new functions.https.HttpsError(
        "invalid-argument",
        "Datos incompletos"
      );
    }

    // Buscar OTP
    const doc = await db
      .collection("email_otps")
      .doc(email)
      .get();

    // No existe
    if (!doc.exists) {

      throw new functions.https.HttpsError(
        "not-found",
        "Código no encontrado"
      );
    }

    const otpData = doc.data();

    // Expirado
    if (Date.now() > otpData.expira) {

      throw new functions.https.HttpsError(
        "deadline-exceeded",
        "Código expirado"
      );
    }

    // Incorrecto
    if (otpData.codigo !== codigo) {

      throw new functions.https.HttpsError(
        "invalid-argument",
        "Código incorrecto"
      );
    }

    // Buscar usuario Firebase Auth
    const user = await admin.auth().getUserByEmail(email);

    // Marcar correo como verificado
    await admin.auth().updateUser(user.uid, {
      emailVerified: true,
    });

    // Eliminar OTP usado
    await db.collection("email_otps")
      .doc(email)
      .delete();

    return {
      success: true,
    };
  }
);

;