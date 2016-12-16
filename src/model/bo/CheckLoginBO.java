/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo;

import daltm.EmailContact;

/**
 *
 * @author TLDs
 */
public class CheckLoginBO {

    public static boolean checkLogin(String username, String password) {
        return new EmailContact().connectMail(username, password);
    }
}
