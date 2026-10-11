package com.nox.minecraftmodshub;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.*;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(15, 17, 25), panel = Color.rgb(27, 30, 43), purple = Color.rgb(124, 77, 255), white = Color.WHITE, muted = Color.rgb(185, 190, 208);
    private LinearLayout list;
    private EditText search;
    private String filter = "Todos";
    static class Mod { String title, edition, description, url; Mod(String t,String e,String d,String u){title=t;edition=e;description=d;url=u;} }
    private final List<Mod> mods = Arrays.asList(
      new Mod("Simple Vehicles Addon", "Bedrock", "Pacote de veículos para explorar o mundo. Confira a versão compatível antes de instalar.", "https://mcpedl.com/simple-vehicles-add-on-bedrock/"),
      new Mod("Carros: Nissan GT-R e 370Z", "Bedrock", "Página de busca de addons de carros. Escolha um arquivo compatível com sua versão.", "https://mcpedl.com/?s=Nissan+R35+GTR+370Z"),
      new Mod("Automobility", "Java", "Mod de veículos para Minecraft Java. Confira carregador e versão compatíveis na página.", "https://modrinth.com/mod/automobility")
    );
    @Override public void onCreate(Bundle state) { super.onCreate(state); getWindow().setStatusBarColor(Color.rgb(15,17,25)); getWindow().setNavigationBarColor(Color.rgb(15,17,25)); build(); }
    private GradientDrawable shape(int color, int radius){ GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius); return d; }
    private TextView text(String s,int size,int color,boolean bold){ TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return t; }
    private void build(){
      ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.setBackgroundColor(bg);
      LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,24,20,28); scroll.addView(root); setContentView(scroll);
      TextView logo=text("▣  MINECRAFT MODS HUB",22,white,true); root.addView(logo);
      TextView sub=text("Seu catálogo de mods para Minecraft",14,muted,false); LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,-2);sp.topMargin=8;root.addView(sub,sp);
      TextView hero=text("ENCONTRE SEU PRÓXIMO MOD",18,white,true); hero.setPadding(18,22,18,8); hero.setBackground(shape(purple,22)); LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.topMargin=22;root.addView(hero,hp);
      TextView hint=text("Escolha a edição e abra a página oficial do mod para conferir os arquivos.",14,white,false); hint.setPadding(18,0,18,18); hint.setBackground(shape(purple,22)); root.addView(hint);
      search=new EditText(this); search.setSingleLine(true); search.setTextColor(white); search.setHintTextColor(muted); search.setHint("Pesquisar mods..."); search.setPadding(16,8,16,8); search.setBackground(shape(panel,16)); LinearLayout.LayoutParams ep=new LinearLayout.LayoutParams(-1,54);ep.topMargin=20;root.addView(search,ep);
      LinearLayout chips=new LinearLayout(this); chips.setOrientation(LinearLayout.HORIZONTAL); chips.setGravity(Gravity.CENTER_VERTICAL); chips.setPadding(0,14,0,12); root.addView(chips);
      for(String c:new String[]{"Todos","Bedrock","Java"}) { Button b=new Button(this); b.setText(c); b.setAllCaps(false); b.setTextColor(white); b.setBackground(shape(c.equals(filter)?purple:panel,30)); LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,48,1);cp.setMargins(3,0,3,0);chips.addView(b,cp); b.setOnClickListener(v->{filter=c; for(int i=0;i<chips.getChildCount();i++){Button x=(Button)chips.getChildAt(i);x.setBackground(shape(x.getText().toString().equals(filter)?purple:panel,30));} render();}); }
      list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);root.addView(list);
      TextView foot=text("Dica: Bedrock e Java usam arquivos diferentes. Faça backup dos seus mundos e baixe apenas de páginas confiáveis.",12,muted,false);foot.setPadding(4,20,4,4);root.addView(foot);
      search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int before,int count){render();}public void afterTextChanged(Editable e){}});
      render();
    }
    private void render(){ if(list==null)return; list.removeAllViews(); String q=search==null?"":search.getText().toString().toLowerCase(Locale.ROOT);
      int count=0; for(Mod m:mods){ if(!filter.equals("Todos")&&!filter.equals(m.edition))continue; if(!(m.title+" "+m.description+" "+m.edition).toLowerCase(Locale.ROOT).contains(q))continue; count++;
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(16,16,16,16);card.setBackground(shape(panel,20));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=10;list.addView(card,p);
        TextView tag=text("●  "+m.edition.toUpperCase(Locale.ROOT),11,purple,true);card.addView(tag);
        TextView title=text(m.title,18,white,true);LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=7;card.addView(title,tp);
        TextView desc=text(m.description,14,muted,false);LinearLayout.LayoutParams dp=new LinearLayout.LayoutParams(-1,-2);dp.topMargin=6;card.addView(desc,dp);
        Button open=new Button(this);open.setText("Ver mod ↗");open.setAllCaps(false);open.setTextColor(white);open.setBackground(shape(purple,14));LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,48);bp.topMargin=12;card.addView(open,bp);open.setOnClickListener(v->{try{startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(m.url)));}catch(Exception e){Toast.makeText(this,"Não foi possível abrir o link.",Toast.LENGTH_SHORT).show();}});
      }
      if(count==0){TextView empty=text("Nenhum mod encontrado. Tente outra pesquisa.",14,muted,false);empty.setPadding(8,24,8,24);list.addView(empty);}
    }
}