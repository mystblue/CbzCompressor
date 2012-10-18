import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CbzCompressor extends JFrame implements DropTargetListener {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        CbzCompressor frame = new CbzCompressor();

	    frame.setTitle("CbzCompressor");
	    frame.setBounds(1000, 10, 300, 300);
	    
	    new DropTarget(frame, frame);
	    
	    frame.setVisible(true);
	}
	
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(DnDConstants.ACTION_COPY);
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    @SuppressWarnings("unchecked")
    public void drop(DropTargetDropEvent dtde) {
        try {
            // �]�����ꂽ�f�[�^�̎擾
            Transferable tr = dtde.getTransferable();
         // �]���f�[�^���T�|�[�g�\�ȃf�[�^���ǂ�������
            if(tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                
                Object obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
                if (obj instanceof List) {
                    List<File> list = (List<File>)obj;
                    int counter = 0;
                    for (File file : list) {
                        if (file.isDirectory()) {
                            ZipCompress.compress(file.getAbsolutePath() + ".cbz", file);
                            counter++;
                        }
                    }
                    String message = String.format("�t�H���_%d�̈��k�������������܂����B", counter);
                    JOptionPane.showMessageDialog(this, message, "����", JOptionPane.INFORMATION_MESSAGE);
                    // �h���b�v����𐳏�I��
                    dtde.getDropTargetContext().dropComplete(true);                }
                } else {
                    dtde.rejectDrop();
                }
        } catch (IOException ex) {
            dtde.rejectDrop();
        } catch (UnsupportedFlavorException ex) {
            dtde.rejectDrop();
        }
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }
}
