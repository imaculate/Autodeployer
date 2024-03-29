@GrabResolver("https://oss.jfrog.org/artifactory/repo")
@Grab("io.ratpack:ratpack-groovy:0.9.0-SNAPSHOT")
import static ratpack.groovy.Groovy.*
import static ratpack.form.Forms.form
import java.util.*;
/**
 * Created by imaculate on 12/31/13.
 */


ratpack {
    handlers {
def userEmail;
/** this will hold the email address of the logged user
 *
 */
def firstName;
/** this will hold the firstName of the user */

def projects;

        def noOfProjects;
def servers;
def noOfServers;



get() {
    render groovyTemplate ( [errormessage:""], "LoginForm.html")
}


post("addproject"){
	def form = parse form()
    def name  = form.name
    def grailsVersion = form.grailsVersion
    def gitRepo = form.gitRepo
    def destination = form.destination

    Project.addProject( userEmail, name , grailsVersion, gitRepo, destination)
	
   projects = Project.getProjects(userEmail)

    noOfProjects = projects.size();


    render groovyTemplate( [message: "You have successfully added a new project",username: firstName, projectCollection: projects, nop: noOfProjects],"HomePage.html")

}





post("login") {


    def form = parse form()	
    def usermail = form.email
    def password = form.password
    if (Users.validateUser(usermail, password)) {
        userEmail = usermail

        firstName = Users.getName(userEmail)
        projects = Project.getProjects(userEmail)

       noOfProjects = projects.size();
		servers = Servers.getAll();
		noOfServers = servers.size();
	def map = [username: firstName, projectCollection: projects, nop: noOfProjects, message:"", errormessage:"", serverCollection: servers, nos : noOfServers] as java.util.Map;



        render groovyTemplate(map,"HomePage.html" )


    } else {
        render groovyTemplate( [errormessage: "Wrong Username or Password"], "LoginForm.html")
    }

}

post("changePassword"){
	def form = parse form()
    def oldPassword = form.oldpassword
    def newPassword = form.p2

    if(Users.validateUser(userEmail,oldPassword)){
        Users.changePassword(userEmail,newPassword)
       render groovyTemplate ( [message: "Your password has been successfully updated", errormessage:""], "ChangePassword.html")
    }else{
       render groovyTemplate( [errormessage: "Please enter correct password", message:""],"ChangePassword.html")
    }

}

get("ChangePassword.html"){
    render groovyTemplate([errormessage: "", message: ""] , "ChangePassword.html")
}

get("AddProject.html"){
    render groovyTemplate("AddProject.html")
}

get("AddServer.html"){
	render groovyTemplate("AddServer.html")
}


post("deleteproject"){
	def form = parse form()
    def id =  form.id
    Project.deleteProject( id)
    projects = Project.getProjects(userEmail)

    noOfProjects = projects.size() ;


    render groovyTemplate([message: "You have successfully deleted this project",username: firstName,
                projectCollection: projects, nop: noOfProjects, errormessage:"", serverCollection: servers, nos : noOfServers], "HomePage.html")

}

post("editproject"){
def form = parse form()
    def id = form.id
    def name  = form.name
    def grailsVersion = form.grailsVersion
    def gitRepo = form.gitRepo
    def destination = form.destination
    
    Project.updateProject( id, name , grailsVersion, gitRepo, destination)
	projects = Project.getProjects(userEmail)

    noOfProjects = projects.size();


   render groovyTemplate( [message: "You have successfully updated this project",username: firstName,
                projectCollection: projects, nop: noOfProjects, errormessage:"", serverCollection: servers, nos : noOfServers], "HomePage.html" )

}

get("EditProject.html"){
	
   	def id = request.queryParams.id
    def name  = request.queryParams.name
    def version = request.queryParams.version
    def git = request.queryParams.gitRepo
    def dest = request.queryParams.dest

    render groovyTemplate([ pid:id, name: name,grailsVersion: version, gitRepo: git , destination:dest], "EditProject.html")

}
post("deploy"){
	def form = parse form()
    def git = form.git;
    def dest = form.dest;
	def version = form.version;
	def folder = git.substring(git.lastIndexOf("/")+1, git.lastIndexOf("."))
	def folderName = folder + "/"
	//def server = dest.substring(0, dest.lastIndexOf(":"))

   
       //if isAdded(server){} else render errormessage that it should be added.
		def message = ""
		def gitProcess = ["bash", "git.sh", folder, git].execute()
		message += gitProcess.err.text
		def deployProcess = ["bash", "deploy.sh", folderName, version, dest].execute()//param4 = remoteHost, param5 = dest
		message += deployProcess.err.text

		if(message == ""){			
			
		
        render groovyTemplate( [message: "You have sucessfully deployed this project",username: firstName, errormessage:"", projectCollection: projects, nop: noOfProjects, serverCollection: servers, nos : noOfServers], "HomePage.html") 
	
                
    }else{
	
	
	render groovyTemplate( [message: "", username: firstName, errormessage:"$message", projectCollection: projects, nop: noOfProjects, serverCollection: servers, nos : noOfServers], "HomePage.html") 
	}
	  

}

post("addServer"){
	def form = parse form()
	def server = form.server
	def pwd = form.pwd
	def message = ""
	
	def addProcess = ["bash", "sshInit.sh", server, pwd].execute()
	message += addProcess.err.text
	message += addProcess.in.text
	if(message == ""){
		Servers.addServer(server, pwd)
		servers = Servers.getAll();
		noOfServers = servers.size();
		render groovyTemplate( [message: "You have sucessfully added this server", username: firstName, errormessage:"", projectCollection: projects, nop: noOfProjects, serverCollection: servers, nos : noOfServers], "HomePage.html")
	}else{
		render groovyTemplate( [message: "", username: firstName, errormessage:"$message", projectCollection: projects, nop: noOfProjects, serverCollection: servers, nos : noOfServers], "HomePage.html")	
	}
}
post("logout"){
    userName = null;
    userEmail = null;
    projects = null;

   noOfProjects = null;

    render groovyTemplate([errormessage:""],"LoginForm.html")

}
        assets "public"
    }
}



