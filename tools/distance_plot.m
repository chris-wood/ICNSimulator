
M1 = csvread('path_5.ccn.message.ContentObject.csv');
M2 = csvread('path_5.ccn.message.Interest.csv');
M3 = csvread('path_5.ccn.message.VirtualInterest.csv');

domain = M1(:,1);
contentValues = M1(:,2);
interestCounts = M2(:,2);
vinterestCounts = M3(:,2);

plot(domain, contentValues, 'DisplayName', 'Content Objects'); 
hold on; 
plot(domain, interestCounts, 'DisplayName', 'Interests'); 
plot(domain, vinterestCounts, 'DisplayName', 'Virtual Interests'); 
hold off;

legend('Content Objects', 'Interests', 'Virtual Interests');
