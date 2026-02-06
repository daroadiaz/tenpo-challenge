function Header() {
  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <img
            src="/tenpo-logo.png"
            alt="Tenpo"
            className="logo-image"
          />
          <div className="badge badge-teal">Panel de Transacciones</div>
        </div>
      </div>
    </header>
  );
}

export default Header;
