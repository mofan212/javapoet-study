package indi.mofan;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import indi.mofan.constant.TestConstants;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mofan
 * @date 2024/1/15 22:55
 */
public class UseGuideTest {
    @Test
    @SneakyThrows
    public void testExample() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld).build();

        javaFile.writeTo(new File(TestConstants.DEFAULT_GENERATED_PATH));
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
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addCode(stringBlock)
                .build();
        methodSpecs.add(main);

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", typeBuilder.addMethods(methodSpecs).build())
                .build();
        javaFile.writeTo(new File(TestConstants.DEFAULT_GENERATED_PATH));
    }
}
