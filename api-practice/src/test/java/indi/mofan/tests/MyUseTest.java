package indi.mofan.tests;

import com.squareup.javapoet.CodeBlock;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

/**
 * @author mofan
 * @date 2024/5/22 13:53
 */
public class MyUseTest implements WithAssertions {
    @Test
    public void testNestedCodeBlock() {
        CodeBlock codeBlock = CodeBlock.of("$T.out.print(\"Hello World\")", System.class);
        CodeBlock nestedCode = CodeBlock.builder()
                .beginControlFlow("if (1 == 1)")
                .addStatement(codeBlock)
                .endControlFlow()
                .build();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    CodeBlock block = CodeBlock.builder()
                            .addStatement("$T str = null", String.class)
                            // 不能使用 addStatement，而是要用 add
                            .addStatement(nestedCode)
                            .build();
                    System.out.println(block);
                });


        String expectGeneratedCode = """
                java.lang.String str = null;
                if (1 == 1) {
                   java.lang.System.out.print("Hello World");
                }
                """.replaceAll("\\s+", "");

        CodeBlock block = CodeBlock.builder()
                .addStatement("$T str = null", String.class)
                .add(nestedCode)
                .build();
        assertThat(block.toString().replaceAll("\\s+", ""))
                .isEqualTo(expectGeneratedCode);
    }
}
