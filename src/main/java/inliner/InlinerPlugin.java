package inliner;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import javax.lang.model.element.Modifier;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class InlinerPlugin implements Plugin {
    @Override
    public String getName() {
        return "Inliner";
    }

    static class Test {

    }

    @Override
    public void init(JavacTask task, String... args) {
        final String basePackage = args[0];

        final Context context = ((BasicJavacTask) task).getContext();
        final JavaFileManager fileManager = context.get(JavaFileManager.class);

        task.addTaskListener(
            new TaskListener() {
                @Override
                public void started(TaskEvent e) {
                }

                @Override
                public void finished(TaskEvent e) {
                    if (e.getKind() == TaskEvent.Kind.ANALYZE) {
                        if (e.getCompilationUnit().getPackageName() == null ||
                            !e.getCompilationUnit().getPackageName().toString().startsWith(basePackage)) {
                            String publicClass = null;
                            for (Tree tree : e.getCompilationUnit().getTypeDecls()) {
                                if (!((JCTree.JCClassDecl) tree).mods.getFlags().contains(Modifier.PUBLIC)) {
                                    continue;
                                }
                                if (publicClass != null) {
                                    throw new AssertionError("Two public classes in one .java file");
                                }
                                publicClass = ((JCTree.JCClassDecl) tree).getSimpleName().toString();
                            }
                            try {
                                HashMap<Name, Name> dependencies = new HashMap<Name, Name>();
                                StringWriter sw = new StringWriter();
                                ((JCTree) e.getCompilationUnit()).accept(new InlinerVisitor(sw, basePackage, true, dependencies));
                                Writer w = fileManager.getFileForOutput(
                                    StandardLocation.CLASS_OUTPUT,
                                    e.getCompilationUnit().getPackageName() == null
                                        ? ""
                                        : e.getCompilationUnit().getPackageName().toString(),
                                    publicClass + ".inl",
                                    e.getSourceFile()).openWriter();
                                for (Map.Entry<Name, Name> entry : dependencies.entrySet()) {
                                    w.write("//" + entry.getKey() + "->" + entry.getValue() + "\n");
                                }
                                w.write("\n");
                                w.write(sw.toString());
                                w.close();
                            } catch (IOException e1) {
                                throw new UncheckedIOException(e1);
                            }
                        } else {
                            for (Tree tree : e.getCompilationUnit().getTypeDecls()) {
                                Name fullName = ((JCTree.JCClassDecl) tree).sym.flatName();
                                ClassTree classTree = (ClassTree) tree;
                                long prevFlags = ((JCTree.JCClassDecl) tree).mods.flags;
                                ((JCTree.JCClassDecl) tree).mods.flags &= ~((long) Flags.PUBLIC);
                                try {
                                    HashMap<Name, Name> dependencies = new HashMap<Name, Name>();
                                    StringWriter sw = new StringWriter();
                                    ((JCTree) tree).accept(new InlinerVisitor(sw, basePackage, false, dependencies));
                                    Writer w = fileManager.getFileForOutput(
                                        StandardLocation.CLASS_OUTPUT,
                                        e.getCompilationUnit().getPackageName().toString(),
                                        classTree.getSimpleName().toString() + ".inl",
                                        e.getSourceFile()).openWriter();
                                    dependencies.remove(fullName);
                                    for (Map.Entry<Name, Name> entry : dependencies.entrySet()) {
                                        w.write("//" + entry.getKey() + "->" + entry.getValue() + "\n");
                                    }
                                    w.write("\n");
                                    w.write(sw.toString());
                                    w.close();
                                } catch (IOException e1) {
                                    throw new UncheckedIOException(e1);
                                }
                                ((JCTree.JCClassDecl) tree).mods.flags = prevFlags;
                            }
                        }
                    }
                }
            });
    }
}
