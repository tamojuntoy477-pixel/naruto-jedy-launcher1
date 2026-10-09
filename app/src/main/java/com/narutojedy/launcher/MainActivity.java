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
    static final String POJAV_PACKAGE = "net.kdt.pojavlaunch";
    static final String POJAV_URL = "https://github.com/PojavLauncherTeam/PojavLauncher/releases";

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

        TextView sub = text("JAVA MOBILE LAUNCHER • v1.1", 13);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        TextView intro = text("Seu portal para o Naruto Jedy no Minecraft Java.", 16);
        intro.setGravity(Gravity.CENTER);
        root.addView(intro);

        status = text("● JAVA: PRONTO PARA VERIFICAR\nPojavLauncher é necessário para iniciar o Minecraft Java.", 14);
        status.setTextColor(Color.LTGRAY);
        root.addView(status);

        Button play = button("▶  ABRIR MINECRAFT JAVA");
        play.setOnClickListener(v -> openJava());
        root.addView(play);

        Button modHelp = button("🍥  COMO INSTALAR O MOD");
        modHelp.setOnClickListener(v -> showModHelp());
        root.addView(modHelp);

        Button server = button("🌐  SERVIDOR JAVA");
        server.setOnClickListener(v -> showServerDialog());
        root.addView(server);

        Button profile = button("👤  PERFIL");
        profile.setOnClickListener(v -> new AlertDialog.Builder(this)
            .setTitle("Perfil Ninja")
            .setMessage("Jogador: Ninja\nLauncher: Naruto Jedy v1.1\nPlataforma: Minecraft Java via PojavLauncher")
            .setPositiveButton("OK", null).show());
        root.addView(profile);

        Button settings = button("⚙  CONFIGURAÇÕES");
        settings.setOnClickListener(v -> showServerDialog());
        root.addView(settings);

        root.addView(text("\n📰 ESTADO DO PROJETO", 18));
        root.addView(text("• Interface do Naruto Jedy\n• Botão para abrir PojavLauncher, se instalado\n• Endereço de servidor guardado no aparelho\n• Guia para instalar mods Java\n\nImportante: este app não contém o Minecraft nem converte automaticamente o addon Bedrock para Java.", 14));

        setContentView(scroll);
    }

    void openJava() {
        Intent launch = getPackageManager().getLaunchIntentForPackage(POJAV_PACKAGE);
        if (launch != null) {
            try {
                startActivity(launch);
            } catch (Exception e) {
                showPojavMissing();
            }
        } else {
            showPojavMissing();
        }
    }

    void showPojavMissing() {
        new AlertDialog.Builder(this)
            .setTitle("PojavLauncher não encontrado")
            .setMessage("Para jogar Minecraft Java no Android, instale primeiro um launcher Java compatível, como o PojavLauncher, e configure seus arquivos de jogo. O Naruto Jedy Launcher ainda não inclui o motor do Minecraft.")
            .setPositiveButton("VER PROJETO POJAV", (d, w) -> {
                try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(POJAV_URL))); }
                catch (Exception ignored) {}
            })
            .setNegativeButton("OK", null).show();
    }

    void showModHelp() {
        new AlertDialog.Builder(this)
            .setTitle("Instalar Naruto Jedy no Java")
            .setMessage("1. Descubra qual versão do Minecraft Java o mod suporta.\n2. Instale o loader exigido pelo mod (Forge ou Fabric).\n3. Abra essa versão uma vez no PojavLauncher.\n4. Coloque o arquivo .jar do mod na pasta mods da instalação.\n5. Inicie a mesma versão.\n\nO arquivo Njedy.zip enviado parece ser um addon Bedrock; ele não vira um mod Java só mudando a extensão. Precisamos de um mod Java compatível ou adaptar o conteúdo.")
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
                    ? "● JAVA: PRONTO PARA VERIFICAR\nPojavLauncher é necessário para iniciar o Minecraft Java."
                    : "● SERVIDOR SALVO\n" + address + "\nAbra o Minecraft Java para conectar.");
            })
            .setNegativeButton("CANCELAR", null).show();
    }
}
