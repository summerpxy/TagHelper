package com.app.motion.taghelper.compile;

import com.app.motion.taghelper.annotation.AutoTag;
import com.app.motion.taghelper.utils.NameUtils;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class AutoTagProcessor extends AbstractProcessor {

    protected Filer mFiler;
    protected Messager mMessager;
    protected Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<String>(Arrays.asList(AutoTag.class.getCanonicalName()));
    }

    private Set<TypeElement> getFilterTypeElement(Set<? extends TypeElement> annotations, Set<? extends Element> elements) {
        Set<TypeElement> result = new HashSet<>();
        for (Element element : elements) {
            if (element instanceof TypeElement) {
                TypeElement te = (TypeElement) element;
                label:
                for (AnnotationMirror mirror : elementUtils.getAllAnnotationMirrors(te)) {
                    for (TypeElement annotation : annotations) {
                        if (mirror.getAnnotationType().asElement().equals(annotation)) {
                            result.add(te);
                            break label;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> result = getFilterTypeElement(annotations, roundEnv.getRootElements());
        for (TypeElement element : result) {
            //对于每一个声明了@AutoTag的class进行处理
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName().toString();
            //内部类的内名字
            ClassName innerClassName = ClassName.get(packageName, className + NameUtils.InnerClassUtils.INNER_CLASS_SUFFIX);
            //开始构建内部类
            TypeSpec.Builder innerClassBuilder = TypeSpec.classBuilder(innerClassName)
                    .addModifiers(Modifier.PUBLIC);

            //构建一个TAG的成员变量
            //public static final String TAG = "";
            ClassName stringClass = ClassName.get("java.lang", "String");
            FieldSpec.Builder tagField = FieldSpec.builder(stringClass, "TAG", Modifier.PUBLIC,
                    Modifier.STATIC)
                    .initializer("$S", " ");
            innerClassBuilder.addField(tagField.build());

            //生成构造函数
            innerClassBuilder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(packageName, className), "activity", Modifier.FINAL)
                    .addStatement("")
                    .build());
            try {
                JavaFile.builder(packageName, innerClassBuilder.build()).build().writeTo(mFiler);
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
            }
        }
        return true;
    }
}
