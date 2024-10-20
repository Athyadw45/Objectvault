import streamlit as st
from time import sleep
from navigation import make_sidebar
import requests
from constants import getConstants

env=getConstants()
objectvault_base_url= f'http://{env["OBJECTVAULT_HOST"]}:{env["OBJECTVAULT_PORT"]}'


make_sidebar()

st.title("Welcome to Objectvault")

st.write("Please log in to continue")   

email = st.text_input("email")
password = st.text_input("Password", type="password")
def login_user_success(email,password):
    r = requests.post(f'{objectvault_base_url}/auth/login', json={'email': email,'password':password})
    if r.status_code==200:
        jwt_token=r.json()['jwtToken']
        st.session_state.logged_in = True
        st.session_state.jwt_token = jwt_token
        return True
    else:
        print(r.raw)
        print(r.status_code)
        st.error("Unable to login :( check credentials or internet connections)")
        return False

if st.button("Log in", type="primary"):
    if login_user_success(email,password):
        st.switch_page("pages/page1.py")

if st.button("Register", type="primary"):
    st.switch_page("pages/register_page.py")



