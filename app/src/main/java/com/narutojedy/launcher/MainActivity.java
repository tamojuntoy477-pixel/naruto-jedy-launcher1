package com.narutojedy.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView status;
    EditText ipField;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        buildUi();
    }

    TextView text(String s, float size) {
        TextView v = new TextView(this);
        v.setText(s); v.setTextColor(Color.WHITE); v.setTextSize(size);
        v.setPadding(0, 10, 0, 10);
        return v;
    }

    Button button(String label) {
        Button b = new Button(this);
        b.setText(label); b.setTextSize(16); b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(116, 20, 190)); bg.setCornerRadius(28);
        b.setBackground(bg);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, 58);
        p.setMargins(0, 8, 0, 8); b.setLayoutParams(p);
        return b;
    }

    void buildUi() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 42, 28, 24);
        root.setBackgroundColor(Color.rgb(8, 10, 18));

        TextView title = text("🌀 NARUTO JEDY", 30);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView sub = text("LAUNCHER MOBILE • v1.0", 13);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        status = text("● SERVIDOR OFFLINE\n\nConfigure o endereço do servidor.", 17);
        status.setTextColor(Color.LTGRAY);
        root.addView(status);

        Button play = button("▶  JOGAR");
        play.setOnClickListener(v -> showPlayDialog());
        root.addView(play);

        Button server = button("🌐  SERVIDOR");
        server.setOnClickListener(v -> showServerDialog());
        root.addView(server);

        Button profile = button("👤  PERFIL");
        profile.setOnClickListener(v -> new AlertDialog.Builder(this)
            .setTitle("Perfil")
            .setMessage("Jogador: Ninja\nStatus: visitante\nVersão do launcher: 1.0")
            .setPositiveButton("OK", null).show());
        root.addView(profile);

        Button settings = button("⚙  CONFIGURAÇÕES");
        settings.setOnClickListener(v -> showServerDialog());
        root.addView(settings);

        root.addView(text("\n📰 NOTÍCIAS", 18));
        root.addView(text("• Naruto Jedy Launcher v1.0\n• Interface mobile pronta\n• Configuração de servidor disponível\n• APK será gerado pelo GitHub Actions", 14));

        setContentView(root);
    }

    void showPlayDialog() {
        if (ipField == null || ipField.getText().toString().trim().isEmpty()) {
            showServerDialog();
            return;
        }
        new AlertDialog.Builder(this)
            .setTitle("Naruto Jedy")
            .setMessage("Servidor configurado em:\n" + ipField.getText().toString().trim() +
                        "\n\nO launcher está pronto para a etapa de integração do cliente Minecraft.")
            .setPositiveButton("OK", null).show();
    }

    void showServerDialog() {
        ipField = new EditText(this);
        ipField.setSingleLine(true);
        ipField.setHint("ex.: 127.0.0.1:25565");
        ipField.setTextColor(Color.WHITE);
        ipField.setHintTextColor(Color.GRAY);

        LinearLayout box = new LinearLayout(this);
        box.setPadding(40, 10, 40, 0);
        box.addView(ipField, new LinearLayout.LayoutParams(-1, 60));

        new AlertDialog.Builder(this)
            .setTitle("Servidor Naruto Jedy")
            .setMessage("Digite IP e porta do servidor:")
            .setView(box)
            .setPositiveButton("SALVAR", (d, w) -> {
                String ip = ipField.getText().toString().trim();
                status.setText(ip.isEmpty() ? "● SERVIDOR OFFLINE" : "● SERVIDOR CONFIGURADO\n\n" + ip);
            })
            .setNegativeButton("CANCELAR", null).show();
    }
}
