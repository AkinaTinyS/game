package com.ral.server.system.thread;

import com.ral.server.game.conf.Cmd;
import com.ral.server.game.conf.Command;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Component
public class LoadGameLogic {


    private static final Map<Integer,Class<?>> cmdCache = new HashMap<>();

    /**
     * 加载协议单例
     * @return
     */
    @PostConstruct
    public boolean load(){
        Set<Class<?>> allClasses = getAllClasses();
        for (Class<?> clazz : allClasses) {
            try {
                Cmd cmd = clazz.getAnnotation(Cmd.class);
                if (cmd != null) {
                    if (cmdCache.get(cmd.code()) != null) {
                        String name = cmdCache.get(cmd.code()).getClass().getName();
                        System.out.println("存在相同协议"+Integer.toHexString(cmd.code()).toUpperCase()+ " exist :" + name + ", new : " + clazz.getName());
                        return false;
                    }
                    cmdCache.put(cmd.code(), clazz);
                    continue;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }


    public static Set<Class<?>> getAllClasses(){

        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageName ="com.ral.server.game.logic";
        String packageDirName = "com/ral/server/game/logic";
        Enumeration<URL> dirs;
        // 是否循环迭代
        boolean recursive = true;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }



    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        // 循环所有文件

        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);

                try {
                    // 添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static Command getCommand(int code) {
        try {
            if(cmdCache.get(code)!=null) {
                return (Command) cmdCache.get(code).newInstance();
            }else {
                System.out.println(code+" code不存在");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
