import streamlit as st
import requests
import time

st.title("Create an account")

firstname = st.text_input("firstname",max_chars=50)
lastname = st.text_input("lastname",max_chars=50)
email = st.text_input("email",max_chars=50)
password = st.text_input("Password", type="password")


def register_user(firstname,lastname,email,password):
    if (firstname is None or not firstname) or (lastname is None or not lastname) or (email is None or not email) or (password is None or not password):
        st.warning("Please check if all the fields are populated")
        return False

    r = requests.post('http://35.208.81.217:3001/auth/register', json={'firstname': firstname,'lastname': lastname,'email': email,'password':password})
    
    if r.status_code != 200:
        st.warning("Unable to register \n"+str(r.content))
        return False
        
    return True


if st.button("Register", type="primary"):
   if register_user(firstname,lastname,email,password):
       st.info("Registration successfull!! Redirecting to Login page")
       time.sleep(3)
       st.switch_page("app.py")

    

    