package bootstrapplugin.wizards;



import java.io.IOException;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;




import bootstrapplugin.bootStrapPlugin;
import bootstrapplugin.bean.ProjectBean;


public class BootStrapWizardPage extends WizardPage implements IWizardPage {
	
    private ProjectBean projectBean;
    private  Text projectNameText;
    private  Text htmlNameText;
    
	private static  final String LABEL_FOR_PROJECT_NAME="Project name";
	private static final String LABEL_FOR_HTMLFILE="html name";
	private static final String INVISIBILITY_LABEL_VALUE="                ";
	
	private static final String ERROR_MESSAGE="Please input correct project name!";
	private static final String MESSAGE_ABOUT_INPUT="Please input something!";
	private static final String PAGE_DESCRIPTION="a very good tool for web developers!";
	private static final String PAGE_TITLE="Web BootStrap";
	protected BootStrapWizardPage(String pageName) {
		super(pageName);	
	}
    
	protected BootStrapWizardPage(String pageName,ProjectBean projectBean){
		this(pageName);
		this.projectBean=projectBean;
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		setImageDescriptor(ImageDescriptor.createFromURL(bootStrapPlugin.
				getDefault().getBundle().getEntry("icons\\pagedescriptor.jpg")));
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container=new Composite(parent,SWT.FILL);
		GridLayout bootstrapFirstPageLayout=new GridLayout();
		bootstrapFirstPageLayout.numColumns=3;
		this.createProjectNameGroupControl(container);
		try {
			this.createDescriptionGroupControl(container);
		} catch (IOException e) {
			e.printStackTrace();
		}
		container.setLayout(bootstrapFirstPageLayout);
		setPageComplete(false);
		 this.addMonitor();
       setControl(parent);
	}
/*
 * create part one
 */
	private void createProjectNameGroupControl(Composite container){
		GridData dataLabel=new GridData();
		dataLabel.horizontalSpan=1;
		Label projectNameLabel=new Label(container,SWT.RIGHT);
		projectNameLabel.setText(LABEL_FOR_PROJECT_NAME);
		projectNameLabel.setLayoutData(dataLabel);
		
		GridData dataText=new GridData();
		dataText.horizontalSpan=1;
		dataText.grabExcessHorizontalSpace=true;
		dataText.horizontalAlignment=GridData.FILL;
        projectNameText=new Text(container,SWT.BORDER|SWT.SINGLE);
		projectNameText.setLayoutData(dataText);
		
		GridData dataInvisibilityLabel=new GridData();
		dataInvisibilityLabel.horizontalSpan=1;
		Label invisibilityLabel=new Label(container,SWT.LEFT);
		invisibilityLabel.setText(INVISIBILITY_LABEL_VALUE);
		 invisibilityLabel.setLayoutData(dataInvisibilityLabel);
	}
	/*
	 * create part two
	 */
	private void createDescriptionGroupControl(Composite container) throws IOException{
		GridData dataLabel=new GridData();
		dataLabel.horizontalSpan=3;
		dataLabel.grabExcessHorizontalSpace=true;
		dataLabel.grabExcessVerticalSpace=true;
		Group descriptionGroup=new Group(container,SWT.SHADOW_IN);
		GridLayout groupLayout=new GridLayout();
		groupLayout.numColumns=3;
		descriptionGroup.setLayout(groupLayout);
		
		//htmlName part
		GridData htmlLabelData=new GridData();
		htmlLabelData.horizontalSpan=1;
		Label htmlName=new Label(descriptionGroup,SWT.CENTER);
		htmlName.setText(LABEL_FOR_HTMLFILE);
		htmlName.setLayoutData(htmlLabelData);
		
		
		GridData htmlTextData=new GridData();
		htmlTextData.horizontalSpan=2;
		htmlTextData.grabExcessHorizontalSpace=true;
		htmlTextData.horizontalAlignment=htmlTextData.FILL;
		htmlNameText=new Text(descriptionGroup,SWT.SINGLE|SWT.BORDER);
		htmlNameText.setLayoutData(htmlTextData);
		
		//image part
		GridData imageData=new GridData();
		imageData.horizontalSpan=3;
        Label descriptionLabel=new Label(descriptionGroup,SWT.CENTER);
        descriptionLabel.setImage(bootStrapPlugin.getICONS(descriptionGroup));
        descriptionLabel.setLayoutData(imageData);
        descriptionLabel.pack();
        
        
        
		descriptionGroup.setLayoutData(dataLabel);
		descriptionGroup.pack();
	}
	
	/**
	 * add some monitors for control
	 */
	private void addMonitor(){
       projectNameText.addKeyListener(new KeyListener(){

			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				String name=projectNameText.getText().trim();
				projectBean.setProjectName(name);
				checkFormant();
			}
			
		});
       htmlNameText.addKeyListener(new KeyListener(){

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			String name=htmlNameText.getText().trim();
			projectBean.setWebFileName(name);
			checkFormant();
		}
    	   
       });
	}
	/*
	 * check the input value is legal or not.
	 */
	private boolean validateText(String name) {
		if(name==null||name.equals("")){
			setMessage(MESSAGE_ABOUT_INPUT);
			return false;
		}
		else if(!name.matches("^[A-Za-z0-9]+$")){
			setErrorMessage(ERROR_MESSAGE);
			return false;
		}
		else{
			setErrorMessage(null);
			setMessage(null);
			return true;
		}
		
	}
	/**
	 * Decide the object is not null and empty.
	 * @return true  if the object is not null and empty.
	 *          false if the object is null or the object's value is empty.
	 */
	private void checkFormant(){
//		String projectName=this.projectBean.getProjectName();
//		String htmlFileName=this.projectBean.getWebFileName();
//		boolean projectNameIsLegal=projectName!=null&&!projectName.equals("");
//		boolean htmlFileNameIsLegal=htmlFileName!=null&&!htmlFileName.equals("");
//		if(htmlFileNameIsLegal&&projectNameIsLegal){
//			return true;
//		}else{
//			return false;
//		}
     	setPageComplete(this.validateText(this.projectBean.getProjectName())
     			&&this.validateText(this.projectBean.getWebFileName()));
	}
}
