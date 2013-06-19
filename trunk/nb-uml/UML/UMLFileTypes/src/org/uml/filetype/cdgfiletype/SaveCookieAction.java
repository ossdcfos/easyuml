package org.uml.filetype.cdgfiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public final class SaveCookieAction implements ActionListener {

    private final SaveCookie context;

    public SaveCookieAction(SaveCookie context) {
        this.context = context;
    }

    public void save(CDGDataObject cdg){
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            FileObject fo = cdg.getPrimaryFile();
            String putanja = fo.getPath();
            fileOut = new FileOutputStream(putanja);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(cdg.getClassDiagram());
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                fileOut.close();
                out.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            context.save();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
