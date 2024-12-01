import * as React from "react";
import { request, setAuthHeader, decodeToken } from "../helpers/axios-helper";

import AdminPage from "./AdminPage";
import StudentPage from "./StudentPage";
import Buttons from "./Buttons";
import AuthContent from "./AuthContent";
import LoginForm from "./LoginForm";
import WelcomeContent from "./WelcomeContent";

export default class AppContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      componentToShow: "welcome",
    };
  }

  login = () => {
    this.setState({ componentToShow: "login" });
  };

  logout = () => {
    this.setState({ componentToShow: "welcome" });
    setAuthHeader(null);
  };

  onLogin = (e, username, password) => {
    e.preventDefault();
    request("POST", "/api/auth/login", {
      username: username,
      password: password,
    })
      .then((response) => {
        const token = response.data.token;
        setAuthHeader(token);

        const decodedToken = decodeToken(token);
        const role = decodedToken?.role; // Assuming `role` is included in the JWT payload

        console.log("User role:", role); // Log the role to the console
        console.log("Decoded Token:", decodedToken);

        if (role === "ROLE_ADMIN") {
          this.setState({ componentToShow: "admin" });
        } else if (role === "ROLE_STUDENT") {
          this.setState({ componentToShow: "student" });
        } else {
          this.setState({ componentToShow: "welcome" }); // Default fallback
        }
      })
      .catch((error) => {
        console.error("Login failed", error);
        setAuthHeader(null);
        this.setState({ componentToShow: "welcome" });
      });
  };

  onRegister = (event, firstName, lastName, username, password) => {
    event.preventDefault();
    request("POST", "/register", {
      firstName: firstName,
      lastName: lastName,
      login: username,
      password: password,
    })
      .then((response) => {
        setAuthHeader(response.data.token);
        this.setState({ componentToShow: "messages" });
      })
      .catch((error) => {
        setAuthHeader(null);
        this.setState({ componentToShow: "welcome" });
      });
  };

  render() {
    const { componentToShow } = this.state;
  
    if (componentToShow === "admin") {
      return <AdminPage onLogout={this.logout} />;
    }
  
    return (
      <>
        <Buttons login={this.login} logout={this.logout} />
        {componentToShow === "welcome" && <WelcomeContent />}
        {componentToShow === "login" && (
          <LoginForm onLogin={this.onLogin} onRegister={this.onRegister} />
        )}
        {componentToShow === "messages" && <AuthContent />}
        {componentToShow === "student" && <StudentPage />}
      </>
    );
  }  
}
