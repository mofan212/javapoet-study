package indi.mofan;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import indi.mofan.constant.TestConstants;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mofan
 * @date 2024/1/15 22:55
 */
public class UseGuideTest {

    private static final String HELLO_WORLD_CLASS_NAME = "HelloWorld";
    private static final File TARGET_FILE = new File(TestConstants.DEFAULT_GENERATED_PATH);

    @Test
    @SneakyThrows
    public void testExample() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder(HELLO_WORLD_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder(TestConstants.DEFAULT_PACKAGE_NAME, helloWorld).build();

        javaFile.writeTo(TARGET_FILE);
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 1")
                .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++)")
                .addStatement("result = result " + op + " i")
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

    @Test
    @SneakyThrows
    public void testCodeAndControlFlow() {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("CodeAndControlFlow")
                .addModifiers(Modifier.PUBLIC);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        // 直接使用文本块作为方法体
        String stringBlock = """
                int total = 0;
                for (int i = 0; i < 10; i++) {
                    total += i;
                }""";
        MethodSpec main1 = MethodSpec.methodBuilder("main1")
                .addCode(stringBlock)
                .build();
        methodSpecs.add(main1);

        // 方便地处理分号、换行、缩进
        MethodSpec main2 = MethodSpec.methodBuilder("main2")
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total +=i")
                .endControlFlow()
                .build();
        methodSpecs.add(main2);

        // 动态地生成代码
        methodSpecs.add(computeRange("multiply10to20", 10, 20, "*"));

        // 使用 `nextControlFlow` 处理 if/else 的分号、换行、缩进
        MethodSpec main3 = MethodSpec.methodBuilder("main3")
                .addStatement("long now = $T.currentTimeMillis()", System.class)
                .beginControlFlow("if ($T.currentTimeMillis() < now)", System.class)
                .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
                .nextControlFlow("else if ($T.currentTimeMillis() == now)", System.class)
                .addStatement("$T.out.println($S)", System.class, "Time stood still!")
                .nextControlFlow("else")
                .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
                .endControlFlow()
                .build();
        methodSpecs.add(main3);

        // 生成 try-catch
        MethodSpec main4 = MethodSpec.methodBuilder("main4")
                .beginControlFlow("try")
                .addStatement("throw new Exception($S)", "Failed")
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow()
                .build();
        methodSpecs.add(main4);

        JavaFile javaFile = JavaFile.builder(TestConstants.DEFAULT_PACKAGE_NAME, typeBuilder.addMethods(methodSpecs).build())
                .build();
        javaFile.writeTo(TARGET_FILE);
    }

    @Test
    @SneakyThrows
    public void test$L() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .returns(int.class)
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = $L; i < $L; i++)", 0, 100)
                .addStatement("result = result $L i", "*")
                .endControlFlow()
                .addStatement("return result")
                .build();

        TypeSpec $L = TypeSpec.classBuilder("$L")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder(TestConstants.DEFAULT_PACKAGE_NAME, $L).build();
        javaFile.writeTo(TARGET_FILE);
    }

    private MethodSpec whatsMyName(String name) {
        return MethodSpec.methodBuilder(name)
                .returns(String.class)
                .addStatement("return $S", name)
                .build();
    }

    @Test
    @SneakyThrows
    public void test$S() {
        TypeSpec $S = TypeSpec.classBuilder("$S")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(List.of(whatsMyName("slimShady"), whatsMyName("eminem"), whatsMyName("marshallMathers")))
                .build();

        JavaFile javaFile = JavaFile.builder(TestConstants.DEFAULT_PACKAGE_NAME, $S)
                .build();

        javaFile.writeTo(TARGET_FILE);
    }

    @Test
    @SneakyThrows
    public void test$T() {
        List<MethodSpec> methodSpecs = new ArrayList<>();

        // 引用现有的类
        MethodSpec methodSpec = MethodSpec.methodBuilder("today")
                .returns(Date.class)
                .addStatement("return new $T()", Date.class)
                .build();
        methodSpecs.add(methodSpec);

        // 引用生成的类
        ClassName helloWorld = ClassName.get(TestConstants.DEFAULT_PACKAGE_NAME, HELLO_WORLD_CLASS_NAME);
        MethodSpec tomorrow = MethodSpec.methodBuilder("tomorrow")
                .returns(helloWorld)
                .addStatement("return new $T()", helloWorld)
                .build();
        methodSpecs.add(tomorrow);

        // 使用 ClassName 定义不同的类
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ParameterizedTypeName listOfHelloWorld = ParameterizedTypeName.get(list, helloWorld);
        MethodSpec beyond = MethodSpec.methodBuilder("beyond")
                .returns(listOfHelloWorld)
                .addStatement("$T result = new $T<>()", listOfHelloWorld, arrayList)
                .addStatement("result.add(new $T())", helloWorld)
                .addStatement("result.add(new $T())", helloWorld)
                .addStatement("return result")
                .build();
        methodSpecs.add(beyond);

        TypeSpec $T = TypeSpec.classBuilder("$T")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(methodSpecs)
                .build();
        JavaFile javaFile = JavaFile.builder(TestConstants.DEFAULT_PACKAGE_NAME, $T).build();
        javaFile.writeTo(TARGET_FILE);
    }
}
