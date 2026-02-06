function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <span>Tenpo Challenge {year}</span>
          <span>Tech Lead Fullstack</span>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
