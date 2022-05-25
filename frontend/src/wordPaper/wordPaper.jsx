
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./wordPaper.css";

//class WordPaper extends React.Component {
function WordPaper(){
    const [datas, setDatas] = useState([
		{
		"word":"ありがとう",
		"mean":"감사합니다",
		"synonym":"こんにちは",
		"read":"아리가또"
		},
		{
		"word":"おひさしぶりですねおひ",
		"mean":"얼굴이 화끈해지다",
		"synonym":"あり",
		"read":"곤니찌와"
		},
		{
		"word":"おねがいします。",
		"mean":"부탁합니다.",
		"synonym":"どういたし",
		"read":"곤니찌와"
		}
	]);
    var datasList = datas;

    useEffect(() => {
    console.log('1234');
    axios.get('https://webhook.site/10df5162-f210-4aae-b221-2f155c347a31')
    .then((Response)=> {
      /*
        Response Data:
          num: 기본키
          word: 단어
          mean: 뜻
          read: 발음
          synonym: 유의어
      */
     console.log(Response);
      datasList = Response.data.datas;
      console.log(datasList);
      console.log('data request success!');
      setDatas(datasList);
    })
    .catch((Error)=>{console.log(Error)})
    }, []);

   //const gridDatas = ["단어1"];
   console.log('data inserting...');
   console.log(datasList)
   const dataList = datasList.map((data, idx) => (
        <div data-layer="6909790e-736b-4068-9bde-22b8aafb2f9e" className="frame" key={idx}>  {/*단어 출력   */}    
                <div data-layer="89ebe899-0e45-4ea6-8f2c-ca4739a7c2c4" className="frame83f7e349">                     
                    <div data-layer="3c2fe693-b4c1-48b0-b9de-f5002ff6e3ae" className="wordea5202cf">                        
                        <div data-layer="ab94a4f6-4906-4ad2-aeb3-1c2fd9a084f9" className="xd9f3fdf8">{datas[idx].synonym}</div>
                        <div data-layer="343914fc-1682-46a3-941b-96330a183255" className="x16d628d6">{datas[idx].word}</div>
                        <div data-layer="76e59f55-5af8-4d36-a431-a01b3546a5eb" className="xe5e28e22">                            
                        <div data-layer="cb4f3478-2577-4f0e-8f57-6e76720fcd45" className="bgd197c3b9"></div>
                            <div data-layer="c09f06c1-e973-4bb6-94a6-e62cf4c9c8ce" className="x5ee0a870">유의어</div>
</div>
                        <svg data-layer="05e05e3d-5039-4cd6-bdd5-e8f9872abca5" preserveAspectRatio="none" viewBox="0 -0.100006103515625 471 0.20001220703125" className="x465a364d58"><path d="M 0 0 L 471 0"  /></svg>
                        <div data-layer="7f93ff02-fe4d-46a3-85f8-ef0bd6e286f3" className="xf63f2ca8">{datas[idx].read}</div>
</div>
                    <div data-layer="2b8a537a-52d4-443b-8afe-1fa775ca1bb2" className="xd106727b">                        
                    <div data-layer="8a5195a4-7500-4a08-bd6c-e291c1484bc6" className="x4154b626">{datas[idx].mean}</div>
                        <div data-layer="a6582daa-f835-469e-a078-c2d0af94769a" className="xb9a31159">                            
                        <div data-layer="49ff9157-f320-48f8-9a97-073867d2ddf1" className="x14234af3f3655">                                
                        <svg data-layer="6b660e44-b7c5-4e19-a147-64f20ad3857d" preserveAspectRatio="none" viewBox="-0.75 -0.75 33.5 33.5" className="x310c67a98e"><path d="M 16 0 C 24.83655548095703 0 32 7.163444519042969 32 16 C 32 24.83655548095703 24.83655548095703 32 16 32 C 7.163444519042969 32 0 24.83655548095703 0 16 C 0 7.163444519042969 7.163444519042969 0 16 0 Z"  /></svg>
                                <svg data-layer="bf8c44f4-60d6-4ec9-9975-2227681e765f" preserveAspectRatio="none" viewBox="-1.414306640625 -1.414215087890625 14.82861328125 14.82843017578125" className="x4423e7f69e"><path d="M 12 0 L 0 12"  /></svg>
                                <svg data-layer="34dc9fa2-91fd-45a8-8f0a-28b4fd6a3bd8" preserveAspectRatio="none" viewBox="-1.414306640625 -1.414215087890625 14.82861328125 14.82843017578125" className="x45f1b00efe"><path d="M 12 12 L 0 0"  /></svg>
                                <div data-layer="584495a5-cdcb-406b-b01f-c0c7ff0651be" className="x471465d67bb6"></div>
</div>
</div>
</div>
</div>
                <svg data-layer="233b2202-bdc8-4fec-b1dd-bdf15fa709b8" preserveAspectRatio="none" viewBox="0 -0.5 1024 1" className="x47abd6047f"><path d="M 0 0 L 1024 0"  /></svg>
</div>
   ));

    return (
          <div data-layer="7d6e9fa5-92dc-49b2-a576-01facdbd08ef" className="wordPaper">        <div data-layer="0d11d7fe-248b-41a2-9ebe-d58ad9bdf6bb" className="x8bc1b3ee">            <div data-layer="543b2d81-1e04-462c-ab33-251107bce603" className="x18458">                <div data-layer="9a657a58-5285-4583-a598-8cdbc7264a1e" className="x18443">                    <svg data-layer="3883662e-6e96-4c05-8b14-55f631b4e252" preserveAspectRatio="none" viewBox="558.3329467773438 -177.66400146484375 56 55.99993896484375" className="bgc41d09a8"><path d="M 558.3329467773438 -177.6640014648438 L 614.3328857421875 -177.6640014648438 L 614.3328857421875 -121.6640625 L 558.3329467773438 -121.6640625 L 558.3329467773438 -177.6640014648438 Z"  /></svg>
                    <svg data-layer="3135f845-73cc-446a-a0a9-670ccfd43d72" preserveAspectRatio="none" viewBox="566.9810791015625 -169.17198181152344 38.044921875 38.35691833496094" className="icon"><path d="M 605.0259399414062 -133.9970550537109 L 595.6788940429688 -143.3440246582031 C 597.7809448242188 -146.0570220947266 599.046875 -149.4490203857422 599.046875 -153.1389923095703 C 599.046875 -161.9790191650391 591.8538818359375 -169.1719818115234 583.013916015625 -169.1719818115234 C 574.1738891601562 -169.1719818115234 566.9810791015625 -161.9790191650391 566.9810791015625 -153.1389923095703 C 566.9810791015625 -144.2980346679688 574.1738891601562 -137.1060180664062 583.013916015625 -137.1060180664062 C 586.5439453125 -137.1060180664062 589.8009033203125 -138.2670135498047 592.451904296875 -140.2070007324219 L 601.8449096679688 -130.8150634765625 L 605.0259399414062 -133.9970550537109 Z M 571.48095703125 -153.1389923095703 C 571.48095703125 -159.4980163574219 576.6538696289062 -164.6719970703125 583.013916015625 -164.6719970703125 C 589.3729248046875 -164.6719970703125 594.546875 -159.4980163574219 594.546875 -153.1389923095703 C 594.546875 -146.7790069580078 589.3729248046875 -141.6060180664062 583.013916015625 -141.6060180664062 C 576.6538696289062 -141.6060180664062 571.48095703125 -146.7790069580078 571.48095703125 -153.1389923095703 Z"  /></svg>
</div>
</div>
            <div data-layer="4c1d8b8c-4101-4ea5-899d-5c231bb3f0c7" className="xf958f200"></div>
</div>
 
        <div data-layer="4977a9d6-9d12-4567-af30-dd60cf318e6b" className="framec6cc90c6">{/*단어 전체 그리드*/}
        {dataList}
</div>
</div>

    );
}



export default WordPaper;
          