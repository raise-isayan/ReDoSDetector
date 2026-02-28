package passive.ast;

import extension.view.base.CaptureItem;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Comment;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.RegExpLiteral;
import passive.signature.RegExPattermItem;

/**
 *
 * @author isayan
 */
public class JavaScriptAnalyze {

    private class RegExpVisitor implements NodeVisitor {

        private final String script;

        public RegExpVisitor(String script) {
            this.script = script;
        }

        @Override
        public boolean visit(AstNode node) {
            // 正規表現リテラルを見つけたら出力
            if (node instanceof RegExpLiteral re) {
                RegExPattermItem item = new RegExPattermItem();
                item.setCaptureValue(getFunctionArgs(this.script, re));
                item.setRegExPattern(unescape(re.getValue()));
                item.setRegExFlag(re.getFlags());
                item.setStart(re.getAbsolutePosition());
                item.setEnd(re.getAbsolutePosition() + re.getLength());
                regexList.add(item);
            } else if (node instanceof NewExpression ne) {
                AstNode target = ne.getTarget();
                // 呼び出し先が "RegExp"
                if (target instanceof Name regexfc) {
                    if ("RegExp".equals(regexfc.getIdentifier())) {
                        List<AstNode> args = ne.getArguments();
                        if (!args.isEmpty()) {
                            String pattern = "";
                            String flags = "";
                            // 第1引数（パターン）の情報を取得
                            if (args.size() >= 1) {
                                pattern = getFunctionArgs(this.script, args.get(0));
                            }
                            if (args.size() >= 2) {
                                flags = getFunctionArgs(this.script, args.get(1));;
                            }
                            RegExPattermItem item = new RegExPattermItem();
                            item.setCaptureValue(getFunctionArgs(this.script, ne));
                            item.setRegExPattern(stripQuotes(pattern));
                            item.setRegExFlag(stripQuotes(flags));
                            item.setStart(ne.getAbsolutePosition());
                            item.setEnd(ne.getAbsolutePosition() + ne.getLength());
                            regexList.add(item);
                        }
                    }
                }
            }
            return true; // 子ノードも走査を続ける
        }
    }

    public static String getFunctionArgs(String script, AstNode node) {
        return script.substring(node.getAbsolutePosition(), node.getAbsolutePosition() + node.getLength());
    }

    // 引用符で囲まれた文字列を剥がす
    private static String stripQuotes(String text) {
        if ((text.startsWith("\"") && text.endsWith("\"")) || (text.startsWith("'") && text.endsWith("'"))) {
            String leteral = text.substring(1, text.length() - 1);
            return unescape(leteral);
        }
        return text;
    }

    private static String stripSlash(String text) {
        if ((text.startsWith("/") && text.endsWith("/"))) {
            String leteral = text.substring(1, text.length() - 1);
            return unescape(leteral);
        }
        return text;
    }

    private static String unescape(String leteral) {
        return leteral.replaceAll("\\\\/", "/").replaceAll("\\\\\\\\", "\\\\");
    }

    public enum AnalyzeOption {
        JS_COMMENTS, REGEXP
    };

    private final List<CaptureItem> commentList = new ArrayList<>();

    public List<CaptureItem> getCommentList() {
        return this.commentList;
    }

    private final List<RegExPattermItem> regexList = new ArrayList<>();

    public List<RegExPattermItem> getRegExpList() {
        return this.regexList;
    }

    private final CompilerEnvirons env;
    private EnumSet<AnalyzeOption> option = EnumSet.noneOf(AnalyzeOption.class);
    private final Parser parser;

    public JavaScriptAnalyze(EnumSet<AnalyzeOption> option) {
        this.option = option;
        this.env = new CompilerEnvirons();
        // 環境設定
        this.env.setRecoverFromErrors(true);
        if (this.option.contains(AnalyzeOption.JS_COMMENTS)) {
            this.env.setRecordingComments(true);
            this.env.setRecordingLocalJsDocComments(true);
        }
        this.parser = new Parser(this.env);
    }

    public void analyze(String script) {
        // パーサーの生成と実行
        AstRoot root = this.parser.parse(script, null, 1);
        // コメントの取得
        if (this.option.contains(AnalyzeOption.JS_COMMENTS) && root.getComments() != null) {
            for (Comment comment : root.getComments()) {
                CaptureItem item = new CaptureItem();
                item.setCaptureValue(comment.getValue());
                item.setStart(comment.getAbsolutePosition());
                item.setEnd(comment.getAbsolutePosition() + comment.getLength());
                this.commentList.add(item);
            }
        }

        if (this.option.contains(AnalyzeOption.REGEXP)) {
            // ASTを走査（ビジターパターン）
            root.visit(new RegExpVisitor(script));
        }
    }

}
