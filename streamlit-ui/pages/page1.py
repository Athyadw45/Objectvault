from navigation import make_sidebar
import streamlit as st
import requests

make_sidebar()

jwt_token = st.session_state.get('jwt_token')

def session_expired():
    st.session_state.logged_in = False
    st.session_state.jwt_token=None
    st.switch_page("app.py")

def list_files():
    endpoint="http://35.208.81.217:3001/v1/list-files"
    headers={"Authorization":f"Bearer {jwt_token}"}
    r = requests.get(url=endpoint,headers=headers)

    if r.status_code==200:
        respBody=r.json()
        filesList = respBody['objectList']
        return filesList
    else:
        session_expired()

filesList = list_files()

st.write(filesList)

        

