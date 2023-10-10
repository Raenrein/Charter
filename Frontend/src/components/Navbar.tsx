import "./css/Navbar.css";
import { useState } from "react";
import "../assets/gear.svg";

const account = JSON.parse(localStorage.getItem("account") || "{}");
const loggedIn = JSON.stringify(account) == "{}" ? false : true;

export default function Navbar() {
  return (
    <nav className="navbar navbar-expand">
      <div className="container">
        <a href="/" className="navbar-brand">
          Charter
        </a>
        <ul className="navbar-nav">
          <NavItem href={loggedIn ? "/u" : "/"} item="Home" />
          {!loggedIn && <NavItem href="/login" item="Login" />}
          <NavItem href="#" item={account.user_name}>
            <DropdownMenu />
          </NavItem>
        </ul>
      </div>
    </nav>
  );
}

function DropdownMenu() {
  function DropdownButton(props: any) {
    return (
      <button className="btn dropdown-btn" onClick={props.func}>
        {props.label}
      </button>
    );
  }

  return (
    <div className="drop-down position-absolute d-flex flex-column p-3 rounded-3">
      {/* <img src="../assets/gear.svg" alt="das" width={20} height={20}/> */}
      <DropdownButton label="Settings" func={() => (location.href = "/settings")} />
      <DropdownButton label="Sign out" func={() => {localStorage.removeItem("account"); location.href = "/";}} />
    </div>
  );
}

function NavItem(props: any) {
  const [open, setOpen] = useState(false);

  return (
    <li className="nav-item">
      <a href={props.href} className="nav-link" onClick={() => setOpen(!open)}>
        {props.item}
      </a>

      {open && props.children}
    </li>
  );
}
