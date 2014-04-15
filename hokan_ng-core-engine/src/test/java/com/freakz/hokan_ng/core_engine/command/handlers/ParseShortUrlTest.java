package com.freakz.hokan_ng.core_engine.command.handlers;

import com.arthurdo.parser.HtmlStreamTokenizer;
import com.freakz.hokan_ng.common.util.HttpPageFetcher;
import junit.framework.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pairio on 15.4.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
public class ParseShortUrlTest {

  @Test
  public void parseTest() throws Exception {
    HttpPageFetcher pageFetcher = new HttpPageFetcher() {
      @Override
      public void fetch(String urlStr) throws Exception {
        htmlBuffer = new StringBuffer(urlStr);
        HtmlStreamTokenizer tok = new HtmlStreamTokenizer(new StringReader(htmlBuffer.toString()));

        textBuffer = new StringBuffer();
        while (tok.nextToken() != HtmlStreamTokenizer.TT_EOF) {

          int ttype = tok.getTokenType();
          if (ttype == HtmlStreamTokenizer.TT_TEXT) {
            textBuffer.append(tok.getStringValue());
          }

        }

        HtmlStreamTokenizer.unescape(textBuffer);
        reset();
      }
    };
    pageFetcher.fetch(sourceHtml);

    String html = pageFetcher.getHtmlBuffer().toString().replaceAll("\n", " ");
    Pattern p = Pattern.compile(".*value = \"(.*?)\">.*");
    Matcher m = p.matcher(html);
    String match = null;
    if (m.matches()) {
      match = m.group(1);
      match = match.substring(0, match.lastIndexOf("/"));

    }
    Assert.assertEquals("http://alturl.com/wuzh7", match);

  }


  public static final String sourceHtml = "\n" +
      "\n" +
      "<html>\n" +
      "<head>\n" +
      "<title>ShortURL.com - free short URL redirection with no ads!</title>\n" +
      "<META NAME=\"description\" CONTENT=\"Free URL redirection service (also known as URL forwarding). Register a free subdomain name and redirect it to your existing URL! No ads, loaded with url redirect features.\">\n" +
      "<META NAME=\"keywords\" CONTENT=\"short url, free url redirection, url forwarding, url redirect, url forward, url redirection, forward url, redirect url, free url, free url forwarding, free sub domain, subdomain, subdomains, URL redrection, subdomain names, subdomain name, sub domains, redirection, redirect\">\n" +
      "<meta name=\"Robots\" content=\"INDEX,FOLLOW\">\n" +
      "<LINK REL=\"SHORTCUT ICON\" HREF=\"http://www.ShortURL.com/ShortURL.ico\">\n" +
      "<BASE HREF=\"http://www.ShortURL.com\">\n" +
      "\n" +
      "<style type=\"text/css\">\n" +
      "<!--\n" +
      "\n" +
      "a.mainNav:link    {  /* Applies to unvisited links of class mainNav */  text-decoration:  none;  font-weight:      bold;  color:            #3366cc;  font-size: 13px;  font-family: Arial, Helvetica, sans-serif;  } a.mainNav:visited {  /* Applies to visited links of class mainNav */  text-decoration:  none;  font-weight:      bold;  color:            #3366cc;  font-size: 13px;  font-family: Arial, Helvetica, sans-serif;  } a.mainNav:hover   {  /* Applies to links under the pointer of class mainNav */  text-decoration:  none;  font-weight:      bold;  color:            red;  font-size: 13px;  font-family: Arial, Helvetica, sans-serif;  } a.mainNav:active  {  /* Applies to activated links of class mainNav */  text-decoration:  none;  font-weight:      bold;  color: #0066FF;  font-size: 13px;  font-family: Arial, Helvetica, sans-serif;  } \n" +
      "\n" +
      ".hide { display: none; }\n" +
      ".hidden { display:none;}\n" +
      ".unhidden { visibility: visible; font: 11 verdana, arial, helvetica, sans-serif;  color: 666666;}\n" +
      "\n" +
      "//-->\n" +
      "</style>\n" +
      "\n" +
      "</head>\n" +
      "\n" +
      "<BODY bgcolor=white LINK=#000099 ALINK=red VLINK=#000099 topmargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n" +
      " \n" +
      "\n" +
      "\n" +
      "<CENTER>\n" +
      "<BR><BR>\n" +
      "<a href=http://www.ShortURL.com><img src=/images_new/newlogo.gif border=0 width=527 height=75 ALT=\"short url - free url redirection service\"></A>\n" +
      "<BR>\n" +
      "\n" +
      "</CENTER>\n" +
      "\n" +
      "\n" +
      "<CENTER>\n" +
      "\n" +
      "<P>\n" +
      "\n" +
      "\n" +
      "\n" +
      "<TABLE WIDTH=750 BGCOLOR=#5A74AF bla=#E65951 border=0 cellpadding=1 cellspacing=0>\n" +
      "<TR><TD>\n" +
      "<TABLE WIDTH=\"748\" bgcolor=#E9F1FD cellspacing=\"0\" cellpadding=\"1\" border=\"0\">\n" +
      "<TR>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com\" class=\"mainNav\">Home</A>\n" +
      "</B></font>\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/signup.html\" class=\"mainNav\">Create</A>\n" +
      "</B></font>\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/login.shtml\" class=\"mainNav\">Log-in</A>\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/infofaq.html\" class=\"mainNav\">FAQ</A>\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/features.php\" class=\"mainNav\">Features</A>\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/contact.html\" class=\"mainNav\">Contact</A>\n" +
      "</TD>\n" +
      "\n" +
      "\n" +
      "<TD align=center width=2>\n" +
      "<font size=2 face=Arial color=#999966><B>\n" +
      " &nbsp;|&nbsp;\n" +
      "</TD>\n" +
      "\n" +
      "<TD align=center width=105>\n" +
      "<font size=2 face=Arial><B>\n" +
      "<A HREF=\"http://www.ShortURL.com/toplinks.html\" class=\"mainNav\">Links</A>\n" +
      "</TD>\n" +
      "\n" +
      "</font>\n" +
      "</TR></TABLE></TD></TR></TABLE>\n" +
      "\n" +
      "<P>\n" +
      "\n" +
      "\n" +
      "\n" +
      "<BR>\n" +
      "\n" +
      "<img src=/images_new/v4createB.gif width=750 height=46 ALT=\"Create\">\n" +
      "<BR>\n" +
      "\n" +
      "<TABLE width=\"740\" cellspacing=\"0\" cellpadding=\"5\" border=\"0\" valign=top>\n" +
      "<TR>\n" +
      "<TD>\n" +
      "\n" +
      "\n" +
      "<script type=\"text/javascript\">\n" +
      "function SelectAll(id)\n" +
      "{\n" +
      "    document.getElementById(id).focus();\n" +
      "    document.getElementById(id).select();\n" +
      "}\n" +
      "\n" +
      "function SelectAll2(id)\n" +
      "{\n" +
      "    document.getElementById(id2).focus();\n" +
      "    document.getElementById(id2).select();\n" +
      "}\n" +
      "\n" +
      "function SelectAll3(id)\n" +
      "{\n" +
      "    document.getElementById(id2).focus();\n" +
      "    document.getElementById(id2).select();\n" +
      "}\n" +
      "</script>\n" +
      "\n" +
      "\n" +
      "<script type=\"text/javascript\">\n" +
      "function unhide(divID) {\n" +
      "  var item = document.getElementById(divID);\n" +
      "  if (item) {\n" +
      "    item.className=(item.className=='hidden')?'unhidden':'hidden';\n" +
      "  }\n" +
      "}\n" +
      "</script>\n" +
      "\n" +
      "<script type=\"text/javascript\">\n" +
      "var opac=0.0;\n" +
      "var alpha=0;\n" +
      "\n" +
      "function init() {\n" +
      "var elem=document.getElementById(\"fadeIn\");\n" +
      "\n" +
      "elem.style.filter=\"alpha(opacity = \" + alpha +\")\";\n" +
      "elem.style.opacity=opac;\n" +
      "\n" +
      "setTimeout(\"fadein()\",50);\n" +
      "}\n" +
      "\n" +
      "function fadein() {\n" +
      "var elem=document.getElementById(\"fadeIn\");\n" +
      "opac=opac+0.1;\n" +
      "alpha=parseInt(opac*100);\n" +
      "elem.style.opacity=opac;\n" +
      "elem.style.filter=\"alpha(opacity = \" + alpha +\")\";\n" +
      "\n" +
      "if (opac < 1.0) {\n" +
      "setTimeout(\"fadein()\",60); //\n" +
      "}\n" +
      "}\n" +
      "\n" +
      "window.onload=init;\n" +
      "</script>\n" +
      "\n" +
      " \n" +
      "<CENTER>\n" +
      "\n" +
      " \n" +
      " \n" +
      "<TABLE width=\"745\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" valign=top>\n" +
      "<TR><TD align=center>\n" +
      " \n" +
      "\t\t \n" +
      "\t\t \n" +
      "\t\t<TABLE  width=\"745\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" valign=top>\n" +
      "\n" +
      "\t\t<TR>\n" +
      "\t\t\n" +
      "\t    <TD align=left valign=top width=\"20\">&nbsp;&nbsp;</TD> \n" +
      "\t\t\n" +
      "\t\t<TD align=left valign=top width=\"210\">\n" +
      "\t\t<font size=2 face=arial color=#6A287E><B>Make a ShortURL instantly<font size=1><BR></font>for any page you're viewing.</B></font><BR>\n" +
      "\t\t\n" +
      "\t\t<font size=1 face=arial color=#6A287E><B>Drag this link: <font STYLE=\"font-size: 12px\"><a href=\"javascript:void(location.href='http://shorturl.com/make_url.php?longurl='+encodeURIComponent(location.href))\">ShortURL!</a></font> to your toolbar. </b></font><BR><font size=1 face=arial><a href=toolbar_bookmark.php>help</A>?<BR></font>\n" +
      " \n" +
      "\t\t</TD>\n" +
      "\t\t\n" +
      "\t\t\n" +
      "\t\t <TD align=center valign=center width=\"*\">\n" +
      "\t\t\n" +
      "\t\n" +
      "\t\t<center> <div id=\"fadeIn\"><font size=2 color=darkgreen face=verdana style=\"color:darkgreen; font-size:13px; font-family:Verdana; font-style:normal; font-weight:bold;\"><B>A SHORTURL HAS BEEN CREATED</B></font></div></center>\n" +
      "\n" +
      "\n" +
      "\t\t</TD>\n" +
      "\n" +
      " \t\t <TD align=center valign=center width=\"90\"> &nbsp;&nbsp;&nbsp;&nbsp;\t</TD>\n" +
      "\t\t\n" +
      "\t\t<TD align=center valign=top width=145>\n" +
      "\t\t<font size=2 face=arial color=green><B>\n" +
      "\t\t<font color=blue>...New...</font><BR>\n" +
      "\t\t Scannable QR Code<BR> for mobile sharing!\n" +
      "\t\t</B></font>\n" +
      "\t\t</TD> \n" +
      " \n" +
      "\t\t</TR> \n" +
      "\t\t\n" +
      "\t\t</TABLE>\n" +
      "\n" +
      "</TD></TR>\n" +
      "</TABLE>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      " \n" +
      "<TABLE width=\"745\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" valign=top>\n" +
      "<TR><TD align=center>\n" +
      "     \n" +
      "\t \n" +
      "\t \n" +
      "\t <TD valign=top align=right width=65>\n" +
      "\t &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<FORM METHOD=POST ACTION=\"make_url.php\">\n" +
      "\t</TD>\t\t\n" +
      "\t\t\t\t\t\n" +
      "\t\t\t\t\t\n" +
      "\t\t\t\t\t\n" +
      "\t<TD align=center>\t\t\t\t\n" +
      "\t \n" +
      "\t\t<TABLE border=0 cellpadding=1 cellspacing=1 width=520>\n" +
      "\t\n" +
      "\t\n" +
      "\t\t\t\t\t\n" +
      "\t\t\t\t\t<TR><TD align=right colspan=2><font size=1></font></TD></TR>\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\n" +
      "\t\t\n" +
      "\t\t\n" +
      "\t\t    <TR>\t\t\t\n" +
      " \n" +
      "\t\t\t\t <TD  align=right>\t\t\t  \n" +
      "\t\t\t\t <font size=\"2\" face=\"Arial\" color=#606060><B>Long URL:</B></font>\n" +
      "\t\t\t\t </TD>\n" +
      "\t\t\t\t <TD align=left>\n" +
      "\t\t\t\t <INPUT NAME=\"longurl\" TYPE=\"text\" SIZE=\"34\" VALUE=\"http://www.hs.fi/kulttuuri/Menik%C3%B6+lapsuutesi+tvn+%C3%A4%C3%A4ress%C3%A4++testaa+muistatko+lastenohjelmien+klassiset+hahmot/a1397471810971\" MAXLENGTH=\"1200\" style=\"background-color: #ffffff; width:320px; font-size:13px; font-face:arial;\" >\n" +
      "\t\t\t\t <input type=\"submit\" value=\"Create\" style=\"background-color: #DEEFC6;   font-size:11px; font-face:arial;\" >\n" +
      "\t\t\t\t </TD></form>\n" +
      "\t\t\t </TR>\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\t<TR><TD align=right colspan=2><font size=1><BR></font></TD></TR>\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\n" +
      "\t\t \n" +
      "\t\t\t\t<TR>\t\t\t\t\t\n" +
      "\t\n" +
      "\t\t\t\t\t\n" +
      "\t\t\t\t\t<TD align=right>\n" +
      "\t\t\t\t\t<font size=\"2\" face=\"Arial\" color=000066><B>Short URL: </B></font>\n" +
      "\t\t\t\t\t</TD>\n" +
      "\t\t\t\t\t<TD align=left>\n" +
      "\t\t\t\t\t<input size=\"50\" type=\"text\" id=\"txtfld\" onClick=\"SelectAll('txtfld');\" \n" +
      "\t\t\t\t\tstyle=\"background-color: #F2FFE0; width:320px; font-size:14px; font-face:arial;\" \n" +
      "\t\t\t\t\tvalue = \"\"http://alturl.com/wuzh7>\n" +
      "\t\t\t\t\t<font size=1>[<a href=http://alturl.com/wuzh7 target=\"_blank\">open</A>]</font>\n" +
      "\t\t\t\t\t</TD>\n" +
      "\t\t\t\t</TR>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\t\t\t\t<TR>\t\t\t\n" +
      "\t\t\t\t\t\t\t\t\t\n" +
      " \n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\t<TD align=right>\n" +
      "\t\t\t\t\t   <font size=\"2\" face=\"Arial\"  color=000066><B>Post/Share: </B></font>\n" +
      "\t\t\t\t\t</TD>\n" +
      "\n" +
      "\t\t\t\t\t\n" +
      "\t\t\t\t\t<TD align=left>\n" +
      "\t\t\t\t\t <a href=\"http://twitter.com/home?status=http%3A%2F%2Falturl.com%2Fwuzh7\" target=\"_blank\" title=\"post to Twitter\"><img src=/images/twitter-icon.png border=0></A>  \n" +
      "\t\t\t\t\t <a href=\"http://www.facebook.com/share.php?u=http%3A%2F%2Falturl.com%2Fwuzh7\" target=\"_blank\"  title=\"post to Facebook\"><img src=/images/facebook-icon.png border=0></A>\n" +
      "\t\t \n" +
      "\t\t\t\t\t <a href=\"mailto:?subject=http%3A%2F%2Falturl.com%2Fwuzh7&body=http%3A%2F%2Falturl.com%2Fwuzh7\" target=\"_blank\"   title=\"email this url\"><img src=/images/email-icon.png border=0   alt=\"email this url\"></A>\n" +
      "\t\t\t\t\t</TD>\n" +
      "\t\t\t\t</TR>\n" +
      "\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\t\t\t\t<TR><TD align=right colspan=2><font size=1><BR></font></TD></TR>\n" +
      " \n" +
      " \n" +
      " \n" +
      "\t\t\t\t\t<TR>\t\t\t\t\n" +
      "\t \t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t<TD align=right>\n" +
      "\t\t\t\t\t\t   <font size=\"1\" face=\"Arial\" color=#303030><B>Give visitors confidence <BR>with a preview transition: </B></font>\n" +
      "\t\t\t\t\t\t</TD>\n" +
      "\t\t\t\t\t\t<TD align=left>\n" +
      "\t\t\t\t\t\t\t<input size=\"50\" type=\"text\" id=\"txtfld2\" onClick=\"SelectAll('txtfld2');\" \n" +
      "\t\t\t\t\t\tstyle=\"background-color: #F2FFE0; width:320px; font-size:13px; font-face:arial;\" \n" +
      "\t\t\t\t\t\tvalue = \"http://preview.alturl.com/wuzh7\">\n" +
      "\t\t\t\t\t\t<font size=1>[<a href=http://preview.alturl.com/wuzh7 target=\"_blank\">open</A>]</font>\n" +
      "\t\t\t\t\t\t</TD>\n" +
      "\t\t\t\t\t</TR>\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\n" +
      "\t\t\t\t\t\t<TR><TD align=right colspan=2><font size=1><BR></font></TD></TR>\n" +
      "\t\t\t\t\t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t<TR>\n" +
      " \t\n" +
      "\t\t\t\t\t\t\t\n" +
      "\t\t\t\t\t\t<TD align=right>\n" +
      "\t\t\t\t\t\t   <font size=\"2\" face=\"Arial\"  color=000066><B>Private stats page: </B></font>\n" +
      "\t\t\t\t\t\t</TD>\n" +
      "\t\t\t\t\t\t<TD align=left>\n" +
      "\t\t\t\t\t\t\t<input size=\"50\" type=\"text\" id=\"txtfld3\" onClick=\"SelectAll('txtfld3');\" \n" +
      "\t\t\t\t\t\tstyle=\"background-color: #F2FFE0; width:320px; font-size:13px; font-face:arial;\" \n" +
      "\t\t\t\t\t\tvalue = \"http://alturl.com/wuzh7/pw=9yqaP\">\n" +
      "\t\t\t\t\t\t<font size=1>[<a href=http://alturl.com/wuzh7/pw=9yqaP target=\"_blank\">open</A>]</font>\n" +
      "\t\t\t\t\t\t</TD>\n" +
      "\t\t\t\t</TR>\n" +
      "\n" +
      "\t\t\t\t<TR>\n" +
      "\t\t\t\t<TD align=center colspan=2>\n" +
      " \n" +
      "\t\t\t\t<BR> \n" +
      "\t\t\t\t \n" +
      "\t\t\t\t<font size=\"2\" face=\"Arial\"><font color=blue><u>Add this ShortURL to your user account</u></font> (coming soon)</font>\n" +
      "\t\t\t\t</TD>\n" +
      "\t\t\t\t</TR>\n" +
      "\t\t\t</TABLE>\n" +
      "\t\t\n" +
      "\t\t\n" +
      "\t\t\n" +
      "\t\t\n" +
      "\n" +
      "\t\t</TD>\n" +
      "\n" +
      "\t\t<TD valign=top align=center  width=145>\n" +
      "\t\t \n" +
      "\n" +
      "\t\t\n" +
      "\t\t<img src=/QRcode.php?s=wuzh7><BR>\n" +
      "\t\t\n" +
      "\t\t<font size=2><a href=\"javascript:unhide('ShowDloadOpts');\">download PNG</A></font>\n" +
      "\t\t\n" +
      "\t\t <div id=\"ShowDloadOpts\" class=\"hidden\">    \t\t\t\n" +
      "\t\t\t <font size=1 color=red>choose a size</font> <BR>\n" +
      "\t\t\t<font size=1> XS [<a href=/QRcode/QRdownload.php?s=wuzh7&p=2 target=\"_blank\">54 x 54</A>]</font> <BR>\n" +
      "\t\t\t<font size=1> S [<a href=/QRcode/QRdownload.php?s=wuzh7&p=3 target=\"_blank\">81 x 81</A>]</font><BR>\n" +
      "\t\t\t<font size=1> M [<a href=/QRcode/QRdownload.php?s=wuzh7&p=6 target=\"_blank\">162 x 162</A>]</font><BR>\n" +
      "\t\t\t<font size=1> L [<a href=/QRcode/QRdownload.php?s=wuzh7&p=10 target=\"_blank\">270 x 270</A>]</font><BR>\n" +
      "\t\t\t<font size=1> XXL [<a href=/QRcode/QRdownload.php?s=wuzh7&p=25 target=\"_blank\">675 x 675</A>]</font><BR>\n" +
      "        </div>\n" +
      " \n" +
      "\t\t</TD>\n" +
      "\t\t\t\n" +
      "\t</TR>\n" +
      "\t</TABLE>\n" +
      "\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "</TD></TR></TABLE>\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "   \n" +
      "   \n" +
      "<TABLE width=\"765\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" valign=top>\n" +
      "<TR><TD align=center>\n" +
      "   \n" +
      "   \n" +
      "    \t\n" +
      "\t\n" +
      "\t<P><BR><BR><BR>\n" +
      " \n" +
      "  \n" +
      "   \n" +
      "   \n" +
      "   \n" +
      "  <TABLE WIDTH=610 BGCOLOR=#DEE9FA border=0 cellpadding=1 cellspacing=0 align=bottom> \n" +
      "\t\t\t\t\t\t\t\n" +
      " <TR><TD>\n" +
      "  <TABLE WIDTH=100% BGCOLOR=#F6F9FE border=0 cellpadding=4 cellspacing=0 align=bottom> \n" +
      " <TR><TD>\n" +
      "\t\t\t\t\t\t\t\t\n" +
      "<font size=\"2\" face=\"Arial\"><B>\n" +
      "\n" +
      "\n" +
      "<CENTER>\n" +
      "\n" +
      "<font size=3 face=arial color=#003366><B>\n" +
      "Turn your website address into something shorter<BR>\n" +
      " with a free sub.domain from ShortURL.com!<BR>\n" +
      "</B></font>\n" +
      "<P>\n" +
      "<font size=2 face=arial>\n" +
      "www.some-service-provider.com/<font color=red>yourname</font>/ can become:\n" +
      "<BR>\n" +
      "<B><font color=red>yourname</font>.shortURL.com &nbsp;<font color=red>you</font>.vze.com  &nbsp;<font color=red>you</font>.2Ya.com etc. \n" +
      "\n" +
      "</font>\n" +
      "<P>\n" +
      "<font size=3 color=#38B0DE face=arial>\n" +
      "<B>ITS FREE FOR LIFE - NO YEARLY DOMAIN FEES</B><BR>\n" +
      "</font>\n" +
      "<font size=2 face=arial color=#003366><B>\n" +
      "Traffic Reports, URL Masking, Path forwarding and many other features.<BR>\n" +
      "\n" +
      "Stay in top-level control of your web presence - update anytime you change sites.\n" +
      "</B>\n" +
      "</font>\n" +
      "</TD></TR></TABLE></TD></TR></TABLE>\n" +
      "\n" +
      "\n" +
      " \n" +
      "   \n" +
      "\t  \n" +
      "<BR>\n" +
      "\n" +
      "<font size=2 color=#000000 face=\"Verdana, Arial\"><B>\n" +
      "Choose a <font color=#0066cc><font color=red>name</font>.domain.com</font>\n" +
      "</B></font>\n" +
      " \n" +
      "\n" +
      " <BR>\n" +
      " \n" +
      "\n" +
      "\t\t\t\t<img src=/images_new/create_subdomain.gif width=386 height=29 ALT=\"Free subdomain registration\">\n" +
      "\t\t\t\t<BR>\n" +
      "\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
      "\n" +
      "\t\t\t\t<tr>\n" +
      "\t\t\t\t<FORM METHOD=POST ACTION=\"checkit.cgi\">\n" +
      "\t\t\t\t<td valign=\"bottom\" align=\"center\"><img src=/images_new/urlboxtop.gif width=376 height=8 hspace=0 vspace=0></TD></TR>\n" +
      "\t\t\t\t<tr><td valign=\"top\" align=\"center\">\n" +
      "\t\t\t\t<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" width=\"328\" valign=top align=\"center\">\n" +
      "\t\t\t\t<tr>\n" +
      "\t\t\t\t<td valign=\"middle\" bgcolor=#7394DE align=\"center\"> \n" +
      "\t\t\t\t<font face=\"Arial\" color=\"#FFFFFF\" size=\"1\"><b>\n" +
      "\n" +
      "\t\t\t\t<CENTER>\n" +
      "\t\t\t\t<font size=2 face=arial color=#ffffff>http://</font>\n" +
      "\n" +
      "\t\t\t\t<INPUT NAME=\"user\" TYPE=\"text\" SIZE=\"9\" VALUE=\"\" MAXLENGTH=\"68\">\n" +
      "\t\t\t\t \n" +
      "\n" +
      "\n" +
      "\t\t\t\t<font size=3 color=#ffffff><B>.</font></B>\n" +
      "\n" +
      "\t\t\t\t<SELECT NAME=\"domain\">\n" +
      "\t\t\t\t<OPTION VALUE=\"shorturl\">ShortURL.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"2ya\">2Ya.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"vze\">vze.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"1sta\">1stA.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"24ex\">24ex.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"hitart\">hitart.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"bigbig\">bigbig.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"2fear\">2fear.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"2hell\">2hell.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"2tunes\">2tunes.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"2freedom\">2freedom.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"2truth\">2truth.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"2savvy\">2savvy.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"2fortune\">2fortune.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"mirrorz\">mirrorz.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"echoz\">echoz.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"ebored\">ebored.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"antiblog\">antiblog.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"headplug\">headplug.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"dealtap\">dealtap.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"filetap\">filetap.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"spyw\">spyw.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"funurl\">funurl.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"alturl\">alturl.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"hereweb\">hereweb.com</OPTION>\n" +
      "\t\t\t\t<OPTION VALUE=\"mp3update\">mp3update.com</OPTION>\n" +
      "\n" +
      "\t\t\t\t<OPTION VALUE=\"ALL\">Check All!</OPTION>\n" +
      "\n" +
      "\t\t\t\t</SELECT>\n" +
      "\t\t\t\t<input type=\"submit\" value=\"Go\">\n" +
      "\t\t\t\t</TD>\n" +
      "\n" +
      "\t\t\t\t</TR>\n" +
      "\t\t\t\t</TABLE>\n" +
      "\t\t\t\t</TD></TR>\n" +
      "\t\t\t\t<tr><td valign=\"bottom\" align=\"center\"><img src=/images_new/urlboxbottom.gif width=376 height=8 hspace=0 vspace=0></TD></TR>\n" +
      "\t\t\t\t</TD></TR></TABLE>\n" +
      "\n" +
      " \n" +
      "\n" +
      " \n" +
      " \n" +
      " \n" +
      " \n" +
      " \n" +
      " <img src=\"/images_new/shortani2.gif\" width=\"334\" height=\"26\" ALT=\"subdomain URL redirection URL forwarding\">\n" +
      "\t\t\t<BR>\n" +
      "</form>\t\t\t\t\t\n" +
      "\n" +
      "</TD>\n" +
      "</TR>\n" +
      "</TABLE>\n" +
      " \n" +
      " <BR><BR>\n" +
      "  \n" +
      "  \n" +
      "  \n" +
      "  \n" +
      "  \n" +
      "\n" +
      "\n" +
      " <BR><BR> <BR><BR>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "</TD>\n" +
      "</TR>\n" +
      "</TABLE>\n" +
      "\n" +
      "\n" +
      "<TABLE width=750  cellpadding=0 cellspacing=0>\n" +
      "<TR>\n" +
      "<TD width=75>\n" +
      "<img src=sepa.gif width=75 height=26>\n" +
      "</TD>\n" +
      "\n" +
      "<BR><BR>\n" +
      "\n" +
      "<TD width=650>\n" +
      "<CENTER>\n" +
      "<font size=2 face=\"Verdana, Arial\">\n" +
      "<a href=\"signup.html\" onMouseOver=\"window.status='Get your own shortURL now!';return true\" onmouseout=\"self.status=''\">\n" +
      "Create Account</A>\n" +
      "|\n" +
      "<a href=\"login.shtml\" onMouseOver=\"window.status='Log into the account management area to access features, check traffic, and more.';return true\" onmouseout=\"self.status=''\">\n" +
      "Member Log In</A>\n" +
      "|\n" +
      "<a href=\"infofaq.html\" onMouseOver=\"window.status='Questions and Answers, Details on shortURL Features';return true\" onmouseout=\"self.status=''\">\n" +
      "Info / FAQ</A>\n" +
      "|\n" +
      "<a href=\"contact.html\" onMouseOver=\"window.status='Ask Questions, Report Problems, Make Suggestions';return true\" onmouseout=\"self.status=''\">\n" +
      "Contact Us</A>\n" +
      "<BR>\n" +
      "<a href=\"terms_of_service.html\">\n" +
      "Terms of Service</A>\n" +
      "|\n" +
      "<a href=\"privacy_policy.html\">\n" +
      "Privacy Policy</A>\n" +
      "<P>\n" +
      "</TD>\n" +
      "<TD width=75>\n" +
      "<img src=sepa2.gif width=75 height=26>\n" +
      "</TD>\n" +
      "\n" +
      "</TR>\n" +
      "</TABLE>\n" +
      "\n" +
      "\n" +
      " \n" +
      "\n" +
      "<P><FONT FACE=Arial,Helvetica COLOR=#000000 SIZE=-2>&nbsp;copyright &copy; 1999 - 2009 <A HREF=URL-redirection.html>ShortURL.Com</A>\n" +
      "</CENTER>\n" +
      "\n" +
      "\n" +
      "\n" +
      "</body></html>   \n" +
      " \n" +
      "\t</body></html>  \n" +
      "\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t \n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n" +
      "\t\n";

}
