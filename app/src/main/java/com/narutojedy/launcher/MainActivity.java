package com.narutojedy.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private static final String MJ_PACKAGE = "git.artdeell.mjlaunch";
    private static final String MJ_URL = "https://play.google.com/store/apps/details?id=git.artdeell.mjlaunch";
    private static final int BG = Color.rgb(8, 10, 18);
    private static final int PANEL = Color.rgb(20, 23, 37);
    private static final int PURPLE = Color.rgb(126, 55, 230);
    private static final int CYAN = Color.rgb(68, 210, 226);
    private static final int WHITE = Color.rgb(245, 246, 255);
    private static final int MUTED = Color.rgb(169, 175, 199);

    private SharedPreferences prefs;
    private TextView launcherStatus;
    private TextView serverSummary;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("ninja_shinobi_legacy", MODE_PRIVATE);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        buildUi();
        refreshStatus();
    }

    private GradientDrawable shape(int color, int radius, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        if (strokeColor != Color.TRANSPARENT) drawable.setStroke(1, strokeColor);
        return drawable;
    }

    private TextView label(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setIncludeFontPadding(true);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private void addGap(LinearLayout parent, int height) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, height));
    }

    private LinearLayout card(LinearLayout parent) {
        LinearLayout panel = vertical();
        panel.setPadding(18, 16, 18, 16);
        panel.setBackground(shape(PANEL, 24, Color.rgb(43, 48, 72)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = 14;
        parent.addView(panel, lp);
        return panel;
    }

    private Button actionButton(String title, boolean primary) {
        Button button = new Button(this);
        button.setText(title);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(WHITE);
        button.setMinHeight(54);
        button.setBackground(shape(primary ? PURPLE : Color.rgb(35, 39, 58), 18,
                primary ? Color.rgb(158, 104, 255) : Color.rgb(57, 64, 91)));
        return button;
    }

    private void addButton(LinearLayout parent, String title, boolean primary, View.OnClickListener click) {
        Button button = actionButton(title, primary);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.topMargin = 8;
        parent.addView(button, lp);
        button.setOnClickListener(click);
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);
        LinearLayout root = vertical();
        root.setPadding(20, 20, 20, 26);
        scroll.addView(root);

        LinearLayout hero = vertical();
        hero.setPadding(20, 24, 20, 22);
        hero.setGravity(Gravity.CENTER_HORIZONTAL);
        hero.setBackground(shape(Color.rgb(26, 18, 48), 28, Color.rgb(95, 55, 155)));
        root.addView(hero, new LinearLayout.LayoutParams(-1, -2));

        TextView emblem = label("忍", 38, CYAN, true);
        emblem.setGravity(Gravity.CENTER);
        hero.addView(emblem);
        addGap(hero, 3);
        TextView title = label("NINJA SHINOBI", 25, WHITE, true);
        title.setGravity(Gravity.CENTER);
        hero.addView(title);
        TextView legacy = label("LEGACY  •  JAVA EDITION", 12, CYAN, true);
        legacy.setGravity(Gravity.CENTER);
        hero.addView(legacy);
        addGap(hero, 8);
        TextView subtitle = label("Seu centro ninja para jogar Minecraft Java no Android.", 14, MUTED, false);
        subtitle.setGravity(Gravity.CENTER);
        hero.addView(subtitle);

        addGap(root, 16);
        LinearLayout statusCard = card(root);
        statusCard.addView(label("STATUS DO LAUNCHER", 12, CYAN, true));
        launcherStatus = label("Verificando MJ Launcher…", 15, WHITE, true);
        statusCard.addView(launcherStatus);
        serverSummary = label("", 13, MUTED, false);
        statusCard.addView(serverSummary);

        LinearLayout playCard = card(root);
        playCard.addView(label("ENTRAR NO JOGO", 18, WHITE, true));
        playCard.addView(label("Abra o MJ Launcher instalado para iniciar sua instalação de Minecraft Java.", 13, MUTED, false));
        addButton(playCard, "▶  ABRIR MINECRAFT JAVA", true, v -> openJava());
        addButton(playCard, "＋  OBTER / ABRIR MJ LAUNCHER", false, v -> openMjPage());

        LinearLayout toolsCard = card(root);
        toolsCard.addView(label("FERRAMENTAS NINJA", 18, WHITE, true));
        toolsCard.addView(label("Acesso rápido às configurações do seu perfil, servidor e mods.", 13, MUTED, false));
        addButton(toolsCard, "🍥  CONFIGURAR NARUTO JEDY / MODS", false, v -> showModHelp());
        addButton(toolsCard, "🌐  MEU SERVIDOR", false, v -> showServerDialog());
        addButton(toolsCard, "📋  COPIAR ENDEREÇO DO SERVIDOR", false, v -> copyServer());
        addButton(toolsCard, "👤  PERFIL DO JOGADOR", false, v -> showProfileDialog());

        LinearLayout settingsCard = card(root);
        settingsCard.addView(label("CONFIGURAÇÕES E AJUDA", 18, WHITE, true));
        addButton(settingsCard, "⚙  SOBRE ESTE APP", false, v -> showAbout());
        addButton(settingsCard, "📖  GUIA DE INSTALAÇÃO", false, v -> showModHelp());

        TextView footer = label("NINJA SHINOBI: LEGACY  •  BUILD 0.2.0\nLauncher auxiliar independente. Minecraft e MJ Launcher são instalados separadamente.", 11, MUTED, false);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(8, 8, 8, 10);
        root.addView(footer);

        setContentView(scroll);
    }

    private void refreshStatus() {
        boolean installed = getPackageManager().getLaunchIntentForPackage(MJ_PACKAGE) != null;
        launcherStatus.setText(installed
                ? "● MJ Launcher detectado"
                : "● MJ Launcher não detectado — instale para jogar");
        launcherStatus.setTextColor(installed ? CYAN : Color.rgb(255, 190, 100));
        String address = prefs.getString("server_address", "");
        serverSummary.setText(address.isEmpty() ? "Servidor salvo: nenhum" : "Servidor salvo: " + address);
    }

    private void openJava() {
        Intent launch = getPackageManager().getLaunchIntentForPackage(MJ_PACKAGE);
        if (launch != null) {
            try { startActivity(launch); }
            catch (Exception e) { showMjMissing(); }
        } else showMjMissing();
    }

    private void openMjPage() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MJ_URL)));
        } catch (Exception e) {
            new AlertDialog.Builder(this).setTitle("Não foi possível abrir a página")
                    .setMessage("Confira sua conexão e tente novamente.")
                    .setPositiveButton("OK", null).show();
        }
    }

    private void showMjMissing() {
        new AlertDialog.Builder(this)
                .setTitle("MJ Launcher não encontrado")
                .setMessage("Este aplicativo organiza seus atalhos, mas não contém o motor do Minecraft. Instale o MJ Launcher, configure nele uma instalação Java compatível e volte para iniciar o jogo.")
                .setPositiveButton("OBTER MJ LAUNCHER", (d, w) -> openMjPage())
                .setNegativeButton("VOLTAR", null).show();
    }

    private void showModHelp() {
        new AlertDialog.Builder(this)
                .setTitle("Guia de mods Java")
                .setMessage("1. Descubra a versão exata do Minecraft exigida pelo mod.\n\n2. Confira o loader necessário (Forge, NeoForge ou Fabric).\n\n3. Configure a mesma versão e o mesmo loader no MJ Launcher.\n\n4. Coloque o arquivo .jar na pasta mods da instalação correta.\n\n5. Abra o jogo e confira o log se ocorrer erro.\n\nImportante: um addon Bedrock .mcpack/.mcaddon não funciona como mod Java só por mudar a extensão. Este app ainda não baixa nem instala mods automaticamente.")
                .setPositiveButton("ENTENDI", null).show();
    }

    private void showServerDialog() {
        EditText field = new EditText(this);
        field.setSingleLine(true);
        field.setHint("ex.: jogar.exemplo.net:25565");
        field.setTextColor(WHITE);
        field.setHintTextColor(MUTED);
        field.setText(prefs.getString("server_address", ""));
        LinearLayout wrap = vertical();
        wrap.setPadding(24, 4, 24, 0);
        wrap.addView(field, new LinearLayout.LayoutParams(-1, 58));
        new AlertDialog.Builder(this)
                .setTitle("Endereço do servidor")
                .setMessage("O endereço fica salvo somente neste aparelho.")
                .setView(wrap)
                .setPositiveButton("SALVAR", (d, w) -> {
                    String value = field.getText().toString().trim();
                    prefs.edit().putString("server_address", value).apply();
                    refreshStatus();
                })
                .setNeutralButton("LIMPAR", (d, w) -> {
                    prefs.edit().remove("server_address").apply();
                    refreshStatus();
                })
                .setNegativeButton("CANCELAR", null).show();
    }

    private void copyServer() {
        String address = prefs.getString("server_address", "");
        if (address.isEmpty()) {
            new AlertDialog.Builder(this).setMessage("Salve primeiro o endereço do servidor.")
                    .setPositiveButton("CONFIGURAR", (d, w) -> showServerDialog())
                    .setNegativeButton("CANCELAR", null).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Minecraft server", address));
        Toast.makeText(this, "Endereço copiado.", Toast.LENGTH_SHORT).show();
    }

    private void showProfileDialog() {
        EditText name = new EditText(this);
        name.setSingleLine(true);
        name.setHint("Nome do jogador");
        name.setTextColor(WHITE);
        name.setHintTextColor(MUTED);
        name.setText(prefs.getString("player_name", "Ninja"));
        LinearLayout wrap = vertical();
        wrap.setPadding(24, 4, 24, 0);
        wrap.addView(name, new LinearLayout.LayoutParams(-1, 58));
        new AlertDialog.Builder(this).setTitle("Perfil Ninja")
                .setMessage("Personalize o nome exibido neste app. Isso não altera o nome da conta Microsoft.")
                .setView(wrap)
                .setPositiveButton("SALVAR", (d, w) -> {
                    String value = name.getText().toString().trim();
                    if (value.isEmpty()) value = "Ninja";
                    prefs.edit().putString("player_name", value).apply();
                    Toast.makeText(this, "Perfil salvo: " + value, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("CANCELAR", null).show();
    }

    private void showAbout() {
        new AlertDialog.Builder(this)
                .setTitle("Ninja Shinobi: Legacy")
                .setMessage("Versão 0.2.0\n\nUm launcher auxiliar com tema ninja, atalho para MJ Launcher, perfil local, endereço de servidor e guia de instalação.\n\nEle não distribui arquivos do Minecraft, não autentica contas e ainda não instala ou atualiza mods automaticamente.")
                .setPositiveButton("OK", null).show();
    }
}
