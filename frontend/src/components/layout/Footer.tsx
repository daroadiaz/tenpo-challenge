function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <span>Tenpo Challenge - TL Fullstack {year}</span>
          <span>Panel de Administracion de Transacciones</span>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
