package net.galvin.orange.core.bytecode.impl;

import net.galvin.orange.core.bytecode.ProxyClazzGenerator;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class ProxyClazzAsmGeneratorImpl implements ProxyClazzGenerator {

    private static ProxyClazzGenerator proxyClazzGenerator = null;

    private ProxyClazzAsmGeneratorImpl(){}

    public static ProxyClazzGenerator get(){
        if(proxyClazzGenerator == null){
            synchronized (ProxyClazzAsmGeneratorImpl.class){
                if(proxyClazzGenerator == null){
                    proxyClazzGenerator = new ProxyClazzAsmGeneratorImpl();
                }
            }
        }
        return proxyClazzGenerator;
    }

    @Override
    public Object generate(Class clazz) {
        String className = clazz.getSimpleName()+"$OrangeImpl";
        String Iter = clazz.getName().replaceAll("\\.", "/");

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classWriter.visit(Opcodes.V1_5,Opcodes.ACC_PUBLIC+Opcodes.ACC_SUPER, className, null, "java/lang/Object", new String[] {Iter});

        //构造器
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //接口中类的方法
        Method[] methodArr = clazz.getMethods();
        for(Method method : methodArr){
            this.makeMethod(classWriter,method);
        }
        classWriter.visitEnd();

        byte[] byteCodeArr = classWriter.toByteArray();
        ProxyClassLoader proxyClassLoader = new ProxyClassLoader();
        Class targetClazz = proxyClassLoader.load(className,byteCodeArr);
        Object target = null;
        try {
            target = targetClazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }

    private void makeMethod(ClassWriter classWriter, Method method){
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC,method.getName(),"(Ljava/lang/String;)Ljava/lang/String;",null,null);
        methodVisitor.visitCode();
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "net/galvin/orange/core/transport/comm/NetTransportProxy", "get", "()Lnet/galvin/orange/core/transport/comm/NetTransportProxy;");
        methodVisitor.visitVarInsn(Opcodes.ALOAD,1);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/galvin/orange/core/transport/comm/NetTransportProxy", "send", "(Ljava/lang/String;)Ljava/lang/String;");
//        methodVisitor.visitVarInsn(Opcodes.ALOAD,1);
        methodVisitor.visitInsn(Opcodes.ARETURN);
        methodVisitor.visitMaxs(3,3);
        methodVisitor.visitEnd();
    }

    /**
     * 类加载器
     */
    private class ProxyClassLoader extends ClassLoader{

        public Class load(String classname, byte[] byteCodeArr){
            return super.defineClass(classname,byteCodeArr,0,byteCodeArr.length);
        }

    }

}
