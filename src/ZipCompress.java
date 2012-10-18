import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipCompress {
    private static final String[] extNames = {".jpg", ".JPG", ".png", ".PNG", ".bmp", ".BMP", ".jpeg", ".JPEG"};
    private static final FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String name) {
            for(String extName : extNames) {
                if(name.endsWith(extName)) {
                    return true;
                }
            }
            return false;
        }
    };

    public static void compress(String zipFilename, File folder) {
		try {
			File zipFile = new File(zipFilename);
			File[] files = { folder };
			ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile);
			zos.setEncoding("Windows-31J");
			try {
				encode(zos, files);
			} finally {
				zos.finish();
				zos.flush();
				zos.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static byte[] buf = new byte[1024];

	private static void encode(ZipArchiveOutputStream zos, File[] files) throws Exception {
		int counter = 1;

		ArrayList<File> list = new ArrayList<File>();
		for (File file : files) {
			list.add(file);
		}
		Collections.sort(list, new Sort());
		for (File file : list) {
			if (file.isDirectory()) {
				encode(zos, file.listFiles(filter));
			} else {
				String filePath = file.getPath().replace('\\', '/');
				System.out.println(filePath);
				String ext = filePath.substring(filePath.lastIndexOf("."));
				String newFileName = String.format("%04d" + ext, counter);
				ZipArchiveEntry ze = new ZipArchiveEntry(newFileName);
				zos.putArchiveEntry(ze);
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				int count = 0;
				while ((count = is.read(buf, 0, 1024)) != -1) {
					zos.write(buf, 0, count);
				}
				is.close();
				zos.closeArchiveEntry();
				counter++;
			}
		}
	}
}
