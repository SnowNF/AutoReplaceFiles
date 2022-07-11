import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2){
            System.err.println("Illegal args\n Usage:用arg2文件夹中的文件覆盖arg1文件夹中对应的文件");
            return;
        }
        String srcPath = new File(args[1]).getPath();
        String destPath = new File(args[0]).getPath();
        System.out.println("OverWrite "+ destPath+" by "+srcPath);
        ArrayList<String> src = GetRelativePath(srcPath);
        ArrayList<String> dest = GetRelativePath(destPath);
        ArrayList<String> errInCopy = new ArrayList<>();
        for (String fileToCopy : dest) {
            String command = "cp: " + srcPath+"/"+fileToCopy+" "+destPath+"/"+fileToCopy;
            try {
                copyFile(srcPath+"/"+fileToCopy,destPath+"/"+fileToCopy);
            } catch (IOException e) {
                errInCopy.add("ERROR:\n"+command+"\n    "+e);
            }
        }
        for (String err : errInCopy) {
            System.err.println(err);
        }
    }

    static String CutPath(String root,String path){
        return path.substring(root.length()+1);
    }

    static ArrayList<String> GetRelativePath(String dir){
        Path2Files path2Files = new Path2Files(new File(dir));
        ArrayList<String> relativePath = new ArrayList<>();
        File[] srcFile = path2Files.getFiles();
        for (File file: srcFile) {
            relativePath.add(CutPath(dir,file.getPath()));
        }
//        for (String path : relativePath) {
//            System.out.println(path);
//        }
        return relativePath;
    }

    private static void copyFile(String source, String dest)
            throws IOException {
        try (InputStream input = new FileInputStream(source); OutputStream output = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        }
    }

}


 class Path2Files {
    File path;
    ArrayList<File> files;
    Path2Files(File path){
        this.path = path;
        files = new ArrayList<>();
    }
    File[] getFiles(){
        dir2files(path.listFiles(),files);
        return files.toArray(new File[0]);
    }
    void dir2files(File[] dir,ArrayList<File> toAdd){
        if(dir == null)
            return;
        for (File file : dir) {
            if (file.isFile()){
                //     System.out.println("Added file : "+file.getName());
                toAdd.add(file);
            }
            if (file.isDirectory()){
                //     System.out.println("A directory : "+file.getName());
                dir2files(file.listFiles(), toAdd);
            }
        }
    }
}