<?php
	session_start();
	require_once('/var/www/default/projetPCD/Shared/MySQLConnection.php');
	$success = array(
		"L'implémentation de l'API a été changé avec succès",
		"Le membre a été supprimé avec succès",
		"Le membre a été édité avec succès"
	
	);
	
	$error = array(
		"L'implémentation de l'API n'a pas pu être changée, impossible d'écrire dans le fichier de config",
		"Le membre n'a pas pu être supprimé",
		"Le membre n'a pas pu être édité"
	);
	
	function getIniFile(){
		$file = parse_ini_file('/var/www/default/projetPCD/config.ini',true);
		return $file;
	}
	
	function setIniFile($array){
		$fichier_save="";
		foreach($array as $key => $groupe_n)
		{
			$fichier_save.="\n[".$key."]";
			foreach($groupe_n as $key => $item_n)
			{
				$fichier_save.="\n".$key."=".$item_n;
			}
		}
		$fichier_save=substr($fichier_save, 1); // On enlève le premier caractère qui est -si vous regardez bien- forcément une entrée à la ligne inutile 
		if(false===@file_put_contents("/var/www/default/projetPCD/config.ini", $fichier_save))
			return false;
		return true;
	}
	
	$ini = getIniFile();
	$impl = $ini['API']['Impl'];
	
	if(isset($_POST['api'])){
		$api = $_POST['api'];
		$ini['API']['Impl']=$api;
		if(setIniFile($ini)===true)
			header('Location:admin.php?success=0');
		else
			header('Location:admin.php?error=0');
	}
	

		if(isset($_POST['setMember'])){
			$mysql = MySQLConnection::getInstance();
			$query = $mysql->prepare("UPDATE members SET mail=:mail,name=:name,firstName=:firstName WHERE login=:login");
			$query->execute(array('mail'=>$_POST['mail'],'name'=>$_POST['name'],'firstName'=>$_POST['firstName'],'login'=>$_POST['login']));
			if($query->rowCount()==1){
				header('Location:admin.php?success=2');
			}else{
				header('Location:admin.php?error=2');
			}
		}
		
		if(isset($_POST['deleteMember'])){
			$mysql = MySQLConnection::getInstance();
			$query = $mysql->prepare("DELETE FROM members WHERE login=:login");
			$query->execute(array('login'=>$_POST['login']));
			if($query->rowCount()==1){
				header('Location:admin.php?success=1');
			}else{
				header('Location:admin.php?error=1');
			}
		}

	
	
	$mysql = MySQLConnection::getInstance();
	$query = $mysql->prepare("SELECT * FROM members");
	$query->execute();
	$members=array();
	while($data = $query->fetch(PDO::FETCH_OBJ)){
		$members[]=$data;
	}
?>

<!doctype html>

<html>

	<head>
		<meta charset="utf-8" />
		<meta name="robots" content="noindex, nofollow">
		<meta name="author" content="Duhal Thierry, Jacquesson Alexis, Rubino David, Deroche Anthony" />
		<title>Administration Projet PCD - Groupe 5</title>
		<script type="text/javascript" src="jquery-1.10.2.min.js"></script>
		<style type="text/css">
			input[type=text]{width:200px;}
		</style>
	</head>

	<body>
		<header>
			
		</header>
		<h2>Administration - projet PCD</h2>
		<section id="content" style="width:80%;margin:auto;padding:10px;">
			
				<?php
					if(isset($_GET['success']) && is_numeric($_GET['success'])){
						echo '<p style="color:green;border:1px solid darkgreen;width:75%;margin:auto;padding:10px;">';
						echo @$success[$_GET['success']];
						echo '</p>';
					}
				?>
				
				<?php
					if(isset($_GET['error']) && is_numeric($_GET['error'])){
						echo '<p style="color:red;border:1px solid darkred;width:75%;margin:auto;padding:10px;">';
						echo @$error[$_GET['error']];
						echo '<br />'.@$_SESSION['pdo'];
						echo '</p>';
					}
				?>

			<form method="post" action="admin.php">
				<p>
					<h4>Implémentation de l'API</h4>
					<input type="radio" name="api" id="gny" value="GNy" <?php if($impl=='GNy') echo 'checked';?>/>
					<label for="gny">Grand Nancy</label><br />
					<input type="radio" name="api" id="otp" value="Otp" <?php if($impl=='Otp') echo 'checked';?>/>
					<label for="otp">Open Trip Planner + Google Maps</label><br />
					<input type="submit" value="Changer l'implémentation"/>
				</p>
			</form>
			<form method="post" action="admin.php">
				<p>
					<h4>Gestion des comptes</h4>
					<form method="post" action="admin.php">
						<select name="members" id="members">
							<option value="default">Choisir membre...</option>
							<?php
								foreach($members as $member){
									echo '<option value="'.md5($member->login).'">'.$member->login.'</option>';
								}
							?>
						</select>
						<script type="text/javascript">
							$(function(){
								$('#members').on('change',function(){
									var val=$('#members option:selected').val();
									$('.member').css('display','none');
									if(val!='default')
										$('#member-'+val).css('display','block');
								});
							});
						</script>
					</form>
						<p>
						<?php
						foreach($members as $member){?>
							<form method="post" action="admin.php">
								<table class="member" id="member-<?php echo md5($member->login);?>" style="display:none;">
									<tr><td>Login : </td><td><input type="hidden" value="<?php echo $member->login;?>" name="login"/><strong><?php echo $member->login;?></strong></td></tr>
									<tr><td>Mail : </td><td><input type="text" value="<?php echo $member->mail;?>" name="mail"/></td></tr>
									<tr><td>Nom : </td><td><input type="text" value="<?php echo $member->name;?>" name="name"/></td></tr>
									<tr><td>Prénom : </td><td><input type="text" value="<?php echo $member->firstName;?>" name="firstName"/></td></tr>
									<tr><td colspan="2"><input type="submit" value="Modifier le membre" name="setMember"/>
									<input type="submit" value="Supprimer le membre" name="deleteMember" onclick="if(!window.confirm('Voulez vous vraiment supprimer le membre <?php echo $member->login;?>? Cette action est irréversible.')) return false;"/>
									</tr>
								</table>
							</form>
							<?php
						}
						?>
						</p>
					
				</p>
			</form>
		</section>
		<footer id="footer">
			
		</footer>
	</body>
</html>
