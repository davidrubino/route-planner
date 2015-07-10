
<?php
	$page=(isset($_GET['p'])) ? $_GET['p'] : "home";
?>

<!doctype html>

<html>

	<head>
		<meta charset="utf-8" />
		<meta name="robots" content="noindex, nofollow">
		<meta name="description" content="Site web du projet PCD du groupe 5" />
		<meta name="author" content="Duhal Thierry, Jacquesson Alexis, Rubino David, Deroche Anthony" />
		<title>Projet PCD - Groupe 5</title>
		<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
		<link rel="stylesheet" href="css/style.css">
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css">
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap-theme.min.css">
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
	</head>

	<body>
		<header>
			<?php
				require_once("includes/menu.html");
			?>
		</header>
		<?php if($page=='javadoc'){?>
				<iframe id="iframe" style="border:0;width:100%;height:100%;" src="doc/index.html" frameborder="0"></iframe>
				<script type="text/javascript" language="javascript"> 
					$('#iframe').css('height', $(window).height()-100+'px');
					$("body").css("overflow", "hidden");
				</script>
		<?php }
		else{?>
			<section id="content">
				<?php
					switch($page) {
						case "home":require_once("includes/home.html");break;
						case "registration":require_once("includes/registration.html");break;
						case "api":require_once("includes/api.html");break;
						case "download":require_once("includes/download.html");break;
						default:require_once("includes/home.html");break;
					}
				?>
			</section>
		<?php
		}?>
		<footer id="footer">
			<!--Copyright © 2013--> 
		</footer>
	</body>

</html>