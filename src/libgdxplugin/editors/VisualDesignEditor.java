package libgdxplugin.editors;


import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.util.RowLayouter;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class VisualDesignEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** Widget used in page 0 */
	private ExpandBar bar;
	
	/** The text editor used in page 1. */
	private TextEditor editor;

	
	
	
	
	public VisualDesignEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createPage0() {
		// create row layout
		Composite composite = new Composite(getContainer(), SWT.NONE);
		RowLayout layout = new RowLayout();
		
		layout.type = SWT.VERTICAL;
		composite.setLayout(layout);
		
		// Add 2 widget to layout
		// expand bar
		RowData palleteData = new RowData(100, SWT.FULL_SELECTION);
		
		bar = new ExpandBar(composite, SWT.V_SCROLL);
		bar.setLayoutData(palleteData);
		
		// create palette layout
		Composite barComposite = new Composite(bar, SWT.NONE);
		GridLayout barLayout = new GridLayout();
		
		barComposite.setLayout(barLayout);
		Button button = new Button(barComposite, SWT.PUSH);
		button.setText("button1");
		
		button = new Button(barComposite, SWT.PUSH);
		button.setText("button2");
		
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("What is your favorite button");
		item0.setHeight(barComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(barComposite);
//		item0.setImage(image);
		
		
		// Create layout to drag and drop
		RowData dragDropData = new RowData(SWT.VERTICAL, SWT.FULL_SELECTION);
		Group group = new Group(composite, SWT.NONE);
		group.setText("Example group");
		group.setLayoutData(dragDropData);
		group.setLayout(new FormLayout());
		
		
		FormData data1 = new FormData();
		data1.left = new FormAttachment(10, 5);
		data1 .right   = new FormAttachment(25,0);
		button = new Button(group, SWT.PUSH);
		button.setText("button 3");
		button.setLayoutData(data1);
		
		FormData data2 = new FormData();
		data2.left     = new FormAttachment(button,5);
		data2.right    = new FormAttachment(90,-5);
		Label label = new Label(group, SWT.NONE);
		label.setText("Label example in group");
		label.setLayoutData(data2);
		
		
		int index =  addPage(composite);
		setPageText(index, "Design");
	}
	
	
	
	
	
	
	/*******************************************************************************************************************************/
	/**
	 * Creates page 2 of the multi-page editor,
	 * which shows the sorted text.
	 */
	void createPage1() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
//		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
//		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Source");
		
	}
	
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		
	}
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
}
