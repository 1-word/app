import logo from './logo.svg';
import './App.css';
import WordPaper from './wordPaper/wordPaper';
import WordPaperMobile from './wordPaperMobile/wordPaperMobile';
import { useMediaQuery } from 'react-responsive';

function App() {
  const isMobile = useMediaQuery({query: "(max-width: 1080px)",});
  const isDesktop = useMediaQuery({query: "(min-width: 1081px)",});
  return (
    <div>
     {/* } {isMobile && <WordPaperMobile></WordPaperMobile>}
      {isDesktop && <WordPaper></WordPaper>} */}
      {
      isMobile ? 
      <WordPaperMobile></WordPaperMobile> : <WordPaper></WordPaper>
      }
    </div>
  );
}

export default App;
