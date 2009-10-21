package org.playframework.playclipse;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public final class Editor {

	private ITextEditor textEditor;

	public Editor(ITextEditor textEditor) {
		this.textEditor = textEditor;
	}

	public static Editor getCurrent(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor instanceof ITextEditor) {
			return new Editor((ITextEditor)editor);
		} else {
			return null;
		}
	}

	public int getCurrentLineNo() {
		return this.getTextSelection().getStartLine();
	}

	public int lineCount() {
		return getDocument().getNumberOfLines();
	}

	public String getTitle() {
		return textEditor.getTitle();
	}

	public String enclosingDirectory() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 2);
	}

	/**
	 *
	 * @return true if the file owned by the editor corresponds to a view
	 */
	public boolean isView() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 3).equals("views");
	}

	/**
	 *
	 * @return true if the file owned by the editor is the conf/routes files
	 */
	public boolean isRoutes() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 1).equals("routes");
	}

	private IPath getFilePath() {
		IFileEditorInput input = (IFileEditorInput) textEditor.getEditorInput();
		return input.getFile().getFullPath();
	}

	public String getLine(int lineNo) {
		IDocument doc = this.getDocument();
		try {
			return doc.get(doc.getLineOffset(lineNo), doc.getLineLength(lineNo));
		} catch (BadLocationException e) {
			return null;
		}
	}

	public String getCurrentLine() {
		return getLine(getCurrentLineNo());
	}

	private ITextSelection getTextSelection() {
		return ((ITextSelection) textEditor.getSelectionProvider().getSelection());
	}

	public IDocument getDocument() {
		return textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
	}

}
