import streamlit as st
from time import sleep
from streamlit.runtime.scriptrunner import get_script_run_ctx
from streamlit.source_util import get_pages


def get_current_page_name():
    ctx = get_script_run_ctx()
    if ctx is None:
        raise RuntimeError("Couldn't get script context")

    pages = get_pages("")

    return pages[ctx.page_script_hash]["page_name"]


def make_sidebar():
    with st.sidebar:
        st.title("Objectvault")
        st.write("Secure file object storage service!")
        st.write("")

        if st.session_state.get("logged_in",False):
            st.page_link("pages/page1.py", label="Secret Company Stuff", icon="🔒")

            st.write("")
            st.write("")

            if st.button("Log out"):
                logout()

        elif get_current_page_name() != "app":
            # If anyone tries to access a secret page without being logged in,
            # redirect them to the login page
            st.switch_page("app.py")


def logout():
    st.session_state.logged_in = False
    st.session_state.jwt_token=None
    st.info("Logged out successfully!")
    st.switch_page("app.py")