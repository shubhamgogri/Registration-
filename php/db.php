<?php 

$host = "localhost";
$dbusername = "root";
$dbPassword = "shubham_yes";
$dbname = "login";

$firstname = $_POST['firstname'];
$lastname = $_POST['lastname'];
$email = $_POST['email'];	
$pass = $_POST['pass'];


echo "Successfully";

if (!empty($firstname) &&!empty($lastname)&& !empty($email) && !empty($pass)) {

	$con = new mysqli($host,$dbusername,$dbPassword,$dbname);
	
	$Sql_Query = "insert into 'registration app' (First Name,Last Name, e-mail, password) values ('$firstname','$lastname','$email',$pass)";

	if(mysqli_query($con,$Sql_Query)){
		echo 'Data Submit Successfully';
	}
	else{
	echo 'Try Again';
	}
	
	mysqli_close($con);
}else{
	echo "Fields Are empty";
}


 ?>
