package com.newnote;

/**
 * Created by administartor on 2017/10/18.
 */

public   class MyTest extends NewGenericsUtils<A> {
     public void getA(){
          A obj = getObj();
//          obj.b="bb";
          System.out.println(obj.b);
     }
}
