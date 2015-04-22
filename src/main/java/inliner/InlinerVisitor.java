package inliner;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.util.Name;

import javax.lang.model.element.ElementKind;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.HashMap;

public class InlinerVisitor extends Pretty {

    private final String basePackage;
    private final boolean isRoot;
    private final HashMap<Name, Name> dependencies;

    public InlinerVisitor(Writer writer, final String basePackage, boolean isRoot, HashMap<Name, Name> dependencies) {
        super(writer, true);
        this.basePackage = basePackage;
        this.isRoot = isRoot;
        this.dependencies = dependencies;
    }
    @Override
    public void visitSelect(JCTree.JCFieldAccess tree) {
        try {
            if (tree.sym != null &&
                tree.sym instanceof Symbol.ClassSymbol &&
                tree.sym.getEnclosingElement().getKind() != ElementKind.CLASS) {
                if (tree.sym.flatName().toString().startsWith(basePackage)) {
                    print(inlinePackage(tree.sym.flatName()));
                    return;
                } else if (!isRoot) {
                    print(tree.sym.flatName());
                    return;
                }
            }
            super.visitSelect(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitIdent(JCTree.JCIdent tree) {
        try {
            if (tree.sym != null &&
                tree.sym instanceof Symbol.ClassSymbol &&
                tree.sym.getEnclosingElement().getKind() != ElementKind.CLASS) {
                if (tree.sym.flatName().toString().startsWith(basePackage)) {
                    print(inlinePackage(tree.sym.flatName()));
                    return;
                } else if (!isRoot) {
                    print(tree.sym.flatName());
                    return;
                }
            }
            super.visitIdent(tree);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl tree) {
        final com.sun.tools.javac.util.Name prevName = tree.name;
        if (tree.sym != null &&
            tree.sym.getEnclosingElement().getKind() != ElementKind.CLASS) {
            if (tree.sym.flatName().toString().startsWith(basePackage)) {
                tree.name = inlinePackage(tree.sym.flatName());
            }
        }
        super.visitClassDef(tree);
        tree.name = prevName;
    }

    @Override
    public void visitNewClass(JCTree.JCNewClass tree) {
        com.sun.tools.javac.util.Name prevDefName = null;
        if (tree.def != null) {
            prevDefName = tree.def.name;
            tree.def.name = null;
        }
        super.visitNewClass(tree);
        if (tree.def != null) {
            tree.def.name = prevDefName;
        }
    }

    private Name inlinePackage(Name name) {
        Name.Table table = name.table;
        Name ret = table.fromString("$" + name.toString().replace(".", "$$"));
        dependencies.put(name, ret);
        return ret;
    }

    @Override
    public void visitImport(JCTree.JCImport tree) {
        if (tree.getQualifiedIdentifier().toString().startsWith(basePackage)) {
            if (!isRoot) {
                throw new AssertionError("Unexpected import statement");
            }
            return;
        }
        super.visitImport(tree);
    }
}
