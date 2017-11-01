@SET /P Message=Entrez votre commentaire significatif:
git add .
git commit -a -m "%Message%"
git push origin master
@pause