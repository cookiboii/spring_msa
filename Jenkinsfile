pipeline{

   agent any

  /*   environment{


    } */
   stages{
      stage('Pull Codes from Github'){
          steps{

             checkout scm


          }

      }
      stage('Build Codes by'){

       steps{

        script{
          sh """
           echo "build stage Start"
            """


        }


       }



      }
   }

}