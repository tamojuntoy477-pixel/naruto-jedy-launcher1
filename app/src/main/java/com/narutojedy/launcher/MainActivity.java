package com.narutojedy.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView status;
    EditText ipField;
    SharedPreferences prefs;
    static final String MJ_PACKAGE = "git.artdeell.mjlaunch";
    static final String MJ_URL = "https://play.google.com/store/apps/details?id=git.artdeell.mjlaunch";

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("naruto_jedy", MODE_PRIVATE);
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
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 32, 28, 24);
        root.setBackgroundColor(Color.rgb(8, 10, 18));
        scroll.setFillViewport(true);
        scroll.addView(root);

        TextView title = text("🌀 NARUTO JEDY", 30);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView sub = text("JAVA MOBILE LAUNCHER • MJ EDITION", 13);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        TextView intro = text("Seu portal ninja para o Minecraft Java no Android.", 16);
        intro.setGravity(Gravity.CENTER);
        root.addView(intro);

        status = text("● MJ LAUNCHER: PRONTO PARA VERIFICAR\nInstale o MJ Launcher para abrir o Minecraft Java.", 14);
        status.setTextColor(Color.LTGRAY);
        root.addView(status);

        Button play = button("▶  ABRIR MINECRAFT JAVA");
        play.setOnClickListener(v -> openJava());
        root.addView(play);

        Button install = button("⬇  INSTALAR / ABRIR MJ LAUNCHER");
        install.setOnClickListener(v -> openMjPage());
        root.addView(install);

        Button modHelp = button("🍥  COMO INSTALAR O MOD");
        modHelp.setOnClickListener(v -> showModHelp());
        root.addView(modHelp);

        Button server = button("🌐  SERVIDOR JAVA");
        server.setOnClickListener(v -> showServerDialog());
        root.addView(server);

        Button profile = button("👤  PERFIL");
        profile.setOnClickListener(v -> new AlertDialog.Builder(this)
            .setTitle("Perfil Ninja")
            .setMessage("Jogador: Ninja\nLauncher: Naruto Jedy • MJ Edition\nPlataforma: Minecraft Java via MJ Launcher")
            .setPositiveButton("OK", null).show());
        root.addView(profile);

        Button settings = button("⚙  CONFIGURAÇÕES");
        settings.setOnClickListener(v -> showServerDialog());
        root.addView(settings);

        root.addView(text("\n📰 ESTADO DO PROJETO", 18));
        root.addView(text("• Interface Naruto Jedy\n• Integração para abrir o MJ Launcher instalado\n• Endereço de servidor guardado no aparelho\n• Guia de instalação de mods Java\n\nImportante: este app é um launcher auxiliar; ele não inclui os arquivos do Minecraft nem instala automaticamente o mod Naruto Jedy.", 14));

        setContentView(scroll);
    }

    void openJava() {
        Intent launch = getPackageManager().getLaunchIntentForPackage(MJ_PACKAGE);
        if (launch != null) {
            try {
                startActivity(launch);
            } catch (Exception e) {
                showMjMissing();
            }
        } else {
            showMjMissing();
        }
    }

    void openMjPage() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MJ_URL)));
        } catch (Exception ignored) {
            new AlertDialog.Builder(this).setMessage("Não foi possível abrir a página do MJ Launcher.")
                .setPositiveButton("OK", null).show();
        }
    }

    void showMjMissing() {
        new AlertDialog.Builder(this)
            .setTitle("MJ Launcher não encontrado")
            .setMessage("Instale o MJ Launcher e configure o Minecraft Java nele. Depois volte aqui e toque em ABRIR MINECRAFT JAVA. Este app não contém o motor do Minecraft.")
            .setPositiveButton("VER MJ LAUNCHER", (d, w) -> openMjPage())
            .setNegativeButton("OK", null).show();
    }

    void showModHelp() {
        new AlertDialog.Builder(this)
            .setTitle("Instalar Naruto Jedy no Java")
            .setMessage("1. Confirme a versão do Minecraft exigida pelo mod.\n2. Confirme se ele usa Forge, Fabric ou outro loader.\n3. No MJ Launcher, configure a mesma versão e loader.\n4. Coloque o arquivo .jar compatível na pasta mods dessa instalação.\n5. Inicie o jogo.\n\nAtenção: um addon Bedrock em .zip não vira automaticamente um mod Java. É necessário um mod Java compatível ou uma adaptação do conteúdo.")
            .setPositiveButton("ENTENDI", null).show();
    }

    void showServerDialog() {
        ipField = new EditText(this);
        ipField.setSingleLine(true);
        ipField.setHint("ex.: servidor.exemplo.net:25565");
        ipField.setTextColor(Color.WHITE);
        ipField.setHintTextColor(Color.GRAY);
        ipField.setText(prefs.getString("server_address", ""));

        LinearLayout box = new LinearLayout(this);
        box.setPadding(40, 10, 40, 0);
        box.addView(ipField, new LinearLayout.LayoutParams(-1, 60));

        new AlertDialog.Builder(this)
            .setTitle("Servidor Minecraft Java")
            .setMessage("Guarde o endereço do servidor para consultar depois:")
            .setView(box)
            .setPositiveButton("SALVAR", (d, w) -> {
                String address = ipField.getText().toString().trim();
                prefs.edit().putString("server_address", address).apply();
                status.setText(address.isEmpty()
                    ? "● MJ LAUNCHER: PRONTO PARA VERIFICAR\nInstale o MJ Launcher para abrir o Minecraft Java."
                    : "● SERVIDOR SALVO\n" + address + "\nAbra o Minecraft Java para conectar.");
            })
            .setNegativeButton("CANCELAR", null).show();
    }
}