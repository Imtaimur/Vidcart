package com.example.shopit.data

data class User( val firstname: String,
                 val lastname:String,
                 val email:String,
                 val imgpath:String =""

)
{constructor():this("","","","")}

