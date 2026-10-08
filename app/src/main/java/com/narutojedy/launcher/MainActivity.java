package com.narutojedy.launcher;
import android.app.*;import android.os.*;import android.graphics.Color;import android.view.*;import android.widget.*;
public class MainActivity extends Activity{
 public void onCreate(Bundle b){super.onCreate(b); LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(28,40,28,28);l.setBackgroundColor(Color.rgb(8,10,16));
 TextView title=t("🌀 NARUTO JEDY",30);l.addView(title);TextView sub=t("LAUNCHER MOBILE • v1.0",14);l.addView(sub);
 TextView status=t("● SERVIDOR ONLINE\n\nJogadores: 0     Versão: 1.0",18);status.setTextColor(Color.WHITE);l.addView(status);
 Button play=new Button(this);play.setText("▶ JOGAR");play.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Naruto Jedy").setMessage("Launcher instalado! Configure o IP/porta do servidor para conectar.").setPositiveButton("OK",null).show());l.addView(play);
 TextView news=t("\n📰 NOTÍCIAS\n\nBem-vindo ao Naruto Jedy!\n\n🧩 RECURSOS\n\n📦 Atualizador\n🎮 Servidor\n👤 Perfil\n🏆 Ranking",17);l.addView(news);setContentView(l);}
 TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextColor(Color.WHITE);v.setTextSize(z);v.setPadding(0,12,0,12);return v;}
}
